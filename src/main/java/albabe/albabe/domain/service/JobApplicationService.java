package albabe.albabe.domain.service;

import albabe.albabe.domain.dto.JobApplicationDto;
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

        jobPost.incrementApplicantCount();
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
}

