package albabe.albabe.domain.service;

import albabe.albabe.domain.dto.CompanyDto;
import albabe.albabe.domain.dto.JobPostResponse;
import albabe.albabe.domain.entity.JobPostEntity;
import albabe.albabe.domain.entity.UserEntity;
import albabe.albabe.domain.repository.JobPostRepository;
import albabe.albabe.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobPostService {

    @Autowired
    private JobPostRepository jobPostRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private S3Service s3Service;

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
                savedJobPost.getWorkCategory(),
                savedJobPost.getWorkType(),
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
                companyDto
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
                companyDto
        );
    }
}
