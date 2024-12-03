package albabe.albabe.domain.service;

import albabe.albabe.domain.dto.CompanyDto;
import albabe.albabe.domain.dto.FilterDto;
import albabe.albabe.domain.dto.JobPostResponse;
import albabe.albabe.domain.entity.JobPostEntity;
import albabe.albabe.domain.entity.TimeTableEntity;
import albabe.albabe.domain.entity.UserEntity;
import albabe.albabe.domain.enums.UserRole;
import albabe.albabe.domain.repository.JobApplicationRepository;
import albabe.albabe.domain.repository.JobPostRepository;
import albabe.albabe.domain.repository.UserRepository;
import albabe.albabe.domain.repository.TimeTableRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.lang.reflect.Field;

@Service
public class JobPostService {

    @Autowired
    private JobPostRepository jobPostRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private TimeTableRepository timetableRepository;



    public List<JobPostResponse> getFilteredJobPosts(FilterDto filterDto) {
        // 필터링된 구인 공고 조회


        List<JobPostEntity> jobPosts = jobPostRepository.findJobPostsByFilter(
                filterDto.getWorkLocations(),
                filterDto.getWorkLocations().size(),
                filterDto.getWorkDays(),
                filterDto.getWorkDays().size(),
                filterDto.getWorkCategories(),
                filterDto.getWorkCategories().size(),
                filterDto.getWorkTimeCategory(),
                filterDto.getWorkTimeCategory().size(),
                filterDto.getWorkTerms(),
                filterDto.getWorkTerms().size()
        );

        // 시간표와 겹치는 구인 공고 제외
        if (filterDto.useTimeTable) {
            Optional<TimeTableEntity> timetable = timetableRepository.findByEmail(filterDto.getEmail());

            jobPosts = jobPosts.stream()
                    .filter(job -> !isOverlappingWithTimetable(job, timetable))
                    .collect(Collectors.toList());
        }
        return convertToDtoList(jobPosts);  // List<JobPostEntity> -> List<JobPostResponse> 변환
    }

    private boolean isOverlappingWithTimetable(JobPostEntity jobPost, Optional<TimeTableEntity> timetable) {
        // 시간표에서 모든 요일의 비트를 계산
        Map<String, Integer> userTimetableBits = calculateTimetableBits(timetable.get());

        // 공고의 workDays와 workTime을 기반으로 비트 계산
        int jobTimeBits = calculateJobTimeBits(jobPost);
        // 겹침 여부 판단
        for (String day : jobPost.getWorkDays()) {
            // 해당 요일의 시간표 비트를 가져와 AND 연산
            if (userTimetableBits.containsKey(day))
                if((userTimetableBits.get(day) & jobTimeBits) != 0) {
                    return true;
                }
        }
        return false;
    }

    // 사용자 시간표에서 각 요일별로 비트를 계산하는 함수
    private Map<String, Integer> calculateTimetableBits(TimeTableEntity timetable) {
        Map<String, Integer> timetableBits = new HashMap<>();

        for (String day : Arrays.asList("월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일")) {
            String timeSlots = getTimeSlotsForDay(timetable, day);
            if (timeSlots != null && !timeSlots.isEmpty()) {
                timetableBits.put(day, convertTimeSlotsToBits(timeSlots));
            }
        }

        return timetableBits;
    }

    // 요일별 시간표 데이터를 가져오는 헬퍼 메서드
    private String getTimeSlotsForDay(TimeTableEntity timetable, String day) {
        switch (day) {
            case "월요일":
                return timetable.getMonday();
            case "화요일":
                return timetable.getTuesday();
            case "수요일":
                return timetable.getWednesday();
            case "목요일":
                return timetable.getThursday();
            case "금요일":
                return timetable.getFriday();
            case "토요일":
                return timetable.getSaturday();
            case "일요일":
                return timetable.getSunday();
            default:
                return null;
        }
    }

    // 시간표 문자열을 비트로 변환
    private int convertTimeSlotsToBits(String timeSlots) {
        int bits = 0;
        for (String time : timeSlots.split(",\\s*")) {
            int hour = Integer.parseInt(time.trim());
            bits |= (1 << hour); // 해당 시간의 비트를 1로 설정
        }
        return bits;
    }

    // 구인 공고의 시간 데이터를 비트로 변환
    private int calculateJobTimeBits(JobPostEntity jobPost) {
        if(jobPost.getWorkTime().equals("any"))
            return 0;
        String[] timeRange = jobPost.getWorkTime().split("~");
        int startHour = Integer.parseInt(timeRange[0].trim());
        int endHour = Integer.parseInt(timeRange[1].trim());

        int bits = 0;


        if (startHour <= endHour) {
            // 일반 시간 범위 (예: 9~18)
            for (int hour = startHour; hour < endHour; hour++) {
                bits |= (1 << hour); // 해당 시간의 비트를 1로 설정
            }
        } else {
            // 야간 시간 범위 (예: 22~6)
            // 첫 번째 범위: 22~23
            for (int hour = startHour; hour < 24; hour++) {
                bits |= (1 << hour);
            }
            // 두 번째 범위: 0~6
            for (int hour = 0; hour < endHour; hour++) {
                bits |= (1 << hour);
            }
        }
        return bits;
    }
    // 공고 생성 메서드
    public JobPostResponse createJobPost(JobPostEntity jobPost, MultipartFile companyImage, String email) {
        // 회사 계정 찾기
        UserEntity company = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회사 계정을 찾을 수 없습니다."));

        // 회사 정보 설정
        jobPost.setCompany(company);

        // 이미지 업로드 및 URL 설정
        if (companyImage != null && !companyImage.isEmpty()) {
            try {
                String imageUrl = s3Service.uploadFile(companyImage);
                jobPost.setCompanyImage(imageUrl); // URL을 엔티티에 저장
            } catch (IOException e) {
                throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
            }
        }

        // JobPostEntity 저장
        JobPostEntity savedJobPost = jobPostRepository.save(jobPost);

        // CompanyDto 생성
        CompanyDto companyDto = new CompanyDto(
                company.getId(),
                company.getEmail(),
                company.getName(),
                company.getImage(),
                company.getRole()
        );

        // JobPostResponse 생성 및 반환
        return new JobPostResponse(
                savedJobPost.getId(),
                savedJobPost.getTitle(),
                savedJobPost.getCompanyName(),
                savedJobPost.getCompanyContent(),
                savedJobPost.getCompanyImage(),
                savedJobPost.getPlace(),
                savedJobPost.getPlaceDetail(),
                savedJobPost.getWorkCategory(),
                savedJobPost.getWorkType(),
                savedJobPost.getWorkTimeCategory(),
                savedJobPost.getPeopleNum(),
                savedJobPost.getCareer(),
                savedJobPost.getWorkTerm(),
                savedJobPost.getWorkDays(),
                savedJobPost.getWorkTime(),
                savedJobPost.getPay(),
                savedJobPost.getGender(),
                savedJobPost.getAge(),
                savedJobPost.getDeadline(),
                savedJobPost.getSubmitMethod(),
                companyDto,
                savedJobPost.getCreatedAt()
        );
    }

    // 공고 조회 (전체)
    public List<JobPostResponse> getAllJobPosts() {
        List<JobPostEntity> jobPosts = jobPostRepository.findAll();
        return jobPosts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<JobPostResponse> getJobPosts(String email) {
        List<JobPostEntity> jobPosts;

        if (email != null && !email.isEmpty()) {
            jobPosts = jobPostRepository.findByCompany_EmailOrderByCreatedAtDesc(email);
            // 이메일이 제공되었지만 해당 이메일로 공고가 조회되지 않는 경우
            if (jobPosts.isEmpty()) {
                throw new IllegalArgumentException("입력한 이메일로 조회된 공고가 없습니다.");
            }
        } else {
            jobPosts = jobPostRepository.findAllByOrderByCreatedAtDesc();
        }
        return jobPosts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    // 공고 조회 (단일)
    public JobPostResponse getJobPostById(Long id) {
        JobPostEntity jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("구인 공고를 찾을 수 없습니다."));
        return convertToDto(jobPost);
    }

    // 공고 수정
    public JobPostResponse updateJobPost(Long id, JobPostEntity updatedJobPost, MultipartFile updatedCompanyImage, String email) {
        // 기존 공고 찾기
        JobPostEntity existingJobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("구인 공고를 찾을 수 없습니다."));

        // 사용자 권한 확인
        if (!existingJobPost.getCompany().getEmail().equals(email)) {
            throw new IllegalArgumentException("해당 구인 공고를 수정할 권한이 없습니다.");
        }

        // 업데이트할 필드 설정
        existingJobPost.setTitle(updatedJobPost.getTitle());
        existingJobPost.setCompanyName(updatedJobPost.getCompanyName());
        existingJobPost.setCompanyContent(updatedJobPost.getCompanyContent());
        existingJobPost.setPlace(updatedJobPost.getPlace());
        existingJobPost.setPlaceDetail(updatedJobPost.getPlaceDetail());
        existingJobPost.setWorkCategory(updatedJobPost.getWorkCategory());
        existingJobPost.setWorkType(updatedJobPost.getWorkType());
        existingJobPost.setPeopleNum(updatedJobPost.getPeopleNum());
        existingJobPost.setCareer(updatedJobPost.getCareer());
        existingJobPost.setWorkTerm(updatedJobPost.getWorkTerm());
        existingJobPost.setWorkDays(updatedJobPost.getWorkDays());
        existingJobPost.setWorkTime(updatedJobPost.getWorkTime());
        existingJobPost.setPay(updatedJobPost.getPay());
        existingJobPost.setGender(updatedJobPost.getGender());
        existingJobPost.setAge(updatedJobPost.getAge());
        existingJobPost.setDeadline(updatedJobPost.getDeadline());
        existingJobPost.setSubmitMethod(updatedJobPost.getSubmitMethod());

        // 이미지 업데이트 (새로운 이미지가 제공된 경우)
        if (updatedCompanyImage != null && !updatedCompanyImage.isEmpty()) {
            try {
                String newImageUrl = s3Service.uploadFile(updatedCompanyImage);
                existingJobPost.setCompanyImage(newImageUrl);
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.", e);
            }
        }

        // 공고 저장
        JobPostEntity savedPost = jobPostRepository.save(existingJobPost);

        // DTO로 변환하여 반환
        return convertToDto(savedPost);
    }

    // 공고 삭제
    @Transactional
    public void deleteJobPost(Long id, String email) {
        JobPostEntity jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("구인 공고를 찾을 수 없습니다."));

        if (!jobPost.getCompany().getEmail().equals(email) && !isAdmin(email) ) {
            throw new IllegalArgumentException("해당 구인 공고를 삭제할 권한이 없습니다.");
        }

        // 연관된 지원 내역 삭제
        jobApplicationRepository.deleteByJobPost(jobPost);

        jobPostRepository.delete(jobPost);
    }

    // 관리자인지 확인하는 메서드
    private boolean isAdmin(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return user.getRole() == UserRole.ADMIN; // UserRole을 이용해 ADMIN 확인
    }

    // 엔티티를 DTO로 변환하는 메서드
    private JobPostResponse convertToDto(JobPostEntity jobPost) {
        CompanyDto companyDto = new CompanyDto(
                jobPost.getCompany().getId(),
                jobPost.getCompany().getEmail(),
                jobPost.getCompany().getName(),
                jobPost.getCompany().getImage(),
                jobPost.getCompany().getRole()
        );

        return new JobPostResponse(
                jobPost.getId(),
                jobPost.getTitle(),
                jobPost.getCompanyName(),
                jobPost.getCompanyContent(),
                jobPost.getCompanyImage(),
                jobPost.getPlace(),
                jobPost.getPlaceDetail(),
                jobPost.getWorkCategory(),
                jobPost.getWorkType(),
                jobPost.getWorkTimeCategory(),
                jobPost.getPeopleNum(),
                jobPost.getCareer(),
                jobPost.getWorkTerm(),
                jobPost.getWorkDays(),
                jobPost.getWorkTime(),
                jobPost.getPay(),
                jobPost.getGender(),
                jobPost.getAge(),
                jobPost.getDeadline(),
                jobPost.getSubmitMethod(),
                companyDto,
                jobPost.getCreatedAt()
        );
    }

    // 최신순 상위 12개 조회
    public List<JobPostResponse> getJobsSortedByLatest() {
        return jobPostRepository.findTop12ByOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 지원자 순 상위 12개 조회
    public List<JobPostResponse> getJobsSortedByPopular() {
        return jobPostRepository.findTop12ByOrderByApplicantCountDesc()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<JobPostResponse> convertToDtoList(List<JobPostEntity> jobPosts) {
        return jobPosts.stream()
                .map(job -> {
                    JobPostResponse response = new JobPostResponse();
                    response.setId(job.getId());
                    response.setTitle(job.getTitle());
                    response.setCompanyName(job.getCompanyName());
                    response.setCompanyContent(job.getCompanyContent());
                    response.setCompanyImage(job.getCompanyImage());
                    response.setPlace(job.getPlace());
                    response.setPlaceDetail(job.getPlaceDetail());
                    response.setWorkCategory(job.getWorkCategory());
                    response.setWorkType(job.getWorkType());
                    response.setWorkTimeCategory(job.getWorkTimeCategory());
                    response.setPeopleNum(job.getPeopleNum());
                    response.setCareer(job.getCareer());
                    response.setWorkTerm(job.getWorkTerm());
                    response.setWorkDays(job.getWorkDays());
                    response.setWorkTime(job.getWorkTime());
                    response.setPay(job.getPay());
                    response.setGender(job.getGender());
                    response.setAge(job.getAge());
                    response.setDeadline(job.getDeadline());
                    response.setSubmitMethod(job.getSubmitMethod());

                    // CompanyDto로 변환
                    if (job.getCompany() != null) {
                        CompanyDto companyDto = new CompanyDto();
                        companyDto.setId(job.getCompany().getId());
                        companyDto.setName(job.getCompany().getName());
                        companyDto.setEmail(job.getCompany().getEmail());
                        companyDto.setRole(job.getCompany().getRole());
                        // 다른 필요한 필드를 추가
                        response.setCompany(companyDto);
                    }

                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<JobPostResponse> searchJobPosts(String keyword) {
        List<JobPostEntity> searchResults = jobPostRepository.searchByTitleOrContent(keyword);
        return searchResults.stream()
                .map(this::convertToDto) // Entity -> DTO 변환
                .collect(Collectors.toList());
    }
}


