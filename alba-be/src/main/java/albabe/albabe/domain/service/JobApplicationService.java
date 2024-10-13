package albabe.albabe.domain.service;

import albabe.albabe.domain.entity.JobApplicationEntity;
import albabe.albabe.domain.entity.JobPostEntity;
import albabe.albabe.domain.entity.UserEntity;
import albabe.albabe.domain.repository.JobApplicationRepository;
import albabe.albabe.domain.repository.JobPostRepository;
import albabe.albabe.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    }

    // 신청 목록 보기
    public List<JobApplicationEntity> getApplicationsForJobPost(Long jobPostId) {
        JobPostEntity jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new IllegalArgumentException("구인 공고를 찾을 수 없습니다."));
        return jobApplicationRepository.findByJobPost(jobPost);
    }
}
