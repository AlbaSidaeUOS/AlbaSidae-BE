package albabe.albabe.domain.service;

import albabe.albabe.domain.dto.CompanyDto;
import albabe.albabe.domain.dto.JobApplicationDto;
import albabe.albabe.domain.dto.JobPostResponse;
import albabe.albabe.domain.entity.JobApplicationEntity;
import albabe.albabe.domain.entity.JobPostEntity;
import albabe.albabe.domain.entity.UserEntity;
import albabe.albabe.domain.repository.JobApplicationRepository;
import albabe.albabe.domain.repository.JobPostRepository;
import albabe.albabe.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private JobPostRepository jobPostRepository;

    @Autowired
    private UserRepository userRepository;

    // 알바 지원 처리
    public void applyForJob(Long jobPostId, String applicantEmail) {
        JobPostEntity jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new IllegalArgumentException("구인 공고를 찾을 수 없습니다."));
        UserEntity applicant = userRepository.findByEmail(applicantEmail)
                .orElseThrow(() -> new IllegalArgumentException("지원자를 찾을 수 없습니다."));

        if (jobApplicationRepository.existsByJobPostAndApplicant(jobPost, applicant)) {
            throw new IllegalArgumentException("이미 지원한 알바입니다.");
        }

        JobApplicationEntity application = new JobApplicationEntity();
        application.setJobPost(jobPost);
        application.setApplicant(applicant);
        application.setResume("이력서 내용");

        jobApplicationRepository.save(application);

        jobPostRepository.save(jobPost);
    }

    public List<JobApplicationDto> getApplicationsForJobPost(Long jobPostId) {
        // 구인 공고에 해당하는 신청 목록 가져오기
        List<JobApplicationEntity> applications = jobApplicationRepository.findByJobPostId(jobPostId);
        if (applications.isEmpty()) {
            throw new IllegalArgumentException("해당 구인 공고에 대한 신청이 없습니다.");
        }

        return applications.stream()
                .map(application -> new JobApplicationDto(
                        application.getId(),
                        application.getResume(),
                        application.getApplicant().getName(),  // 신청자의 이름
                        application.getApplicant().getEmail(), // 신청자의 이메일
                        application.getJobPost().getTitle()    // 구인 공고의 제목
                )).collect(Collectors.toList());
    }

    public List<JobPostResponse> getAppliedJobPosts(String email) {
        List<JobPostEntity> jobPosts = jobApplicationRepository.findJobPostsByUserEmail(email);

        // 지원 내역이 없을 경우 예외 처리
        if (jobPosts.isEmpty()) {
            throw new IllegalArgumentException("해당 이메일로 지원한 공고가 없습니다.");
        }

        // 각 JobPostEntity를 JobPostResponse로 변환
        return jobPosts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

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
}

