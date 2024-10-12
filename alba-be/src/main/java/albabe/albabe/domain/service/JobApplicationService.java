package albabe.albabe.domain.service;

import albabe.albabe.domain.entity.JobApplicationEntity;
import albabe.albabe.domain.entity.JobPostEntity;
import albabe.albabe.domain.entity.UserEntity;
import albabe.albabe.domain.enums.UserRole;
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

    public JobApplicationEntity applyForJob(Long jobPostId, String email, String resume) {
        JobPostEntity jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new RuntimeException("구인 공고를 찾을 수 없습니다."));

        UserEntity applicant = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!applicant.getRole().equals(UserRole.PERSONAL)) {
            throw new RuntimeException("개인 회원만 알바를 신청할 수 있습니다.");
        }

        JobApplicationEntity application = new JobApplicationEntity();
        application.setJobPost(jobPost);
        application.setApplicant(applicant);
        application.setResume(resume);

        return jobApplicationRepository.save(application);
    }

    public List<JobApplicationEntity> getApplicationsForJobPost(Long jobPostId, String email) {
        JobPostEntity jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new RuntimeException("구인 공고를 찾을 수 없습니다."));

        if (!jobPost.getCompany().getEmail().equals(email)) {
            throw new RuntimeException("이 공고의 신청 목록을 볼 권한이 없습니다.");
        }

        return jobApplicationRepository.findByJobPost(jobPost);
    }
}
