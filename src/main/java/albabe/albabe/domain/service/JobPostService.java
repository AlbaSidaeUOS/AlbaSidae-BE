package albabe.albabe.domain.service;

import albabe.albabe.domain.dto.CompanyDto;
import albabe.albabe.domain.dto.FilterDto;
import albabe.albabe.domain.dto.JobPostResponse;
import albabe.albabe.domain.entity.JobPostEntity;
import albabe.albabe.domain.entity.TimeTableEntity;
import albabe.albabe.domain.entity.UserEntity;
import albabe.albabe.domain.repository.JobPostRepository;
import albabe.albabe.domain.repository.UserRepository;
import albabe.albabe.domain.repository.TimeTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Collection;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class JobPostService {

    @Autowired
    private JobPostRepository jobPostRepository;

    @Autowired
    private UserRepository userRepository;

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

        System.out.println(filterDto.getWorkTimeCategory());
        System.out.println(filterDto.getWorkTimeCategory().size());

        // 시간표와 겹치는 구인 공고 제외
        if (filterDto.useTimeTable) {
            Optional<TimeTableEntity> timetable = timetableRepository.findByUserId(filterDto.getId());
            jobPosts = jobPosts.stream()
                    .filter(job -> !isOverlappingWithTimetable(job, timetable))
                    .collect(Collectors.toList());
        }

        return convertToDtoList(jobPosts);  // List<JobPostEntity> -> List<JobPostResponse> 변환
    }

    private boolean isOverlappingWithTimetable(JobPostEntity jobPost, Optional<TimeTableEntity> timetable) {
        List<String> jobDays = jobPost.getWorkDays();

        String[] workTimeRange = jobPost.getWorkTime().split("~");
        int jobStartTime = Integer.parseInt(workTimeRange[0]) - 9;
        int jobEndTime = Integer.parseInt(workTimeRange[1]) - 9;

        for (String day : jobDays) {
            List<Integer> timetableHours = getHours(timetable, day);

            for (int hour = jobStartTime; hour < jobEndTime; hour++) {
                if (timetableHours != null && timetableHours.contains(hour)) {
                    return true; // 겹치는 시간이 있는 경우
                }
            }
        }
        return false; // 겹치는 시간이 없는 경우
    }

    public List<Integer> getHours(Optional<TimeTableEntity> timeTableEntity, String day) {
        try {
            // TimeTableEntity 클래스의 필드들 중에서 day에 해당하는 필드를 찾음
            Field field = timeTableEntity.getClass().getDeclaredField(day.toLowerCase()); // day 문자열을 소문자로 바꿔서 필드명에 맞게 접근
            field.setAccessible(true);  // private 필드에도 접근 가능하도록 설정
            return (List<Integer>) field.get(timeTableEntity);  // 해당 필드의 값을 가져와서 반환
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;  // 필드가 없거나 접근할 수 없으면 null 반환
        }
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
        jobPost.setWorkTimeCategory(workTimeToWorkTimeCategory(jobPost.getWorkTime()));

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

        existingJobPost.setWorkTimeCategory(workTimeToWorkTimeCategory(existingJobPost.getWorkTime()));
        // 공고 저장
        JobPostEntity savedPost = jobPostRepository.save(existingJobPost);

        // DTO로 변환하여 반환
        return convertToDto(savedPost);
    }

    // 공고 삭제
    public void deleteJobPost(Long id, String email) {
        JobPostEntity jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("구인 공고를 찾을 수 없습니다."));

        if (!jobPost.getCompany().getEmail().equals(email)) {
            throw new IllegalArgumentException("해당 구인 공고를 삭제할 권한이 없습니다.");
        }

        jobPostRepository.delete(jobPost);
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
    //workTime을 workTimeCateogry 문자열로 바꾸는 함수
    public List<String> workTimeToWorkTimeCategory(String workTime)
    {
        List<String> workTimeCategory = new ArrayList<String>();
        boolean[] isWorkTime = new boolean[24];
        String[] workTimeRange = workTime.split("~");
        Integer[] times = new Integer[2];
        times[0] = Integer.parseInt(workTimeRange[0]);
        times[1] = Integer.parseInt(workTimeRange[1]);
        while(true)
        {
            isWorkTime[times[0]] = true;
            times[0]++;
            if(times[0].equals(times[1]))
                break;
            if(times[0] > 24)
                times[0] = 0;

        }
        for(int i=0; i<24; i++)
        {
            if(i < 6 && isWorkTime[i])
            {
                workTimeCategory.add("새벽");
                i = 6;
            }
            if(i < 12 && isWorkTime[i])
            {
                workTimeCategory.add("오전");
                i = 12;
            }
            if(i < 18 && isWorkTime[i])
            {
                workTimeCategory.add("오후");
                i = 18;
            }
            if(i < 24 && isWorkTime[i])
            {
                workTimeCategory.add("저녁");
                i = 24;
            }
        }
        return workTimeCategory;
    }
}


