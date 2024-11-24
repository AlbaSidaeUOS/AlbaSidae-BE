package albabe.albabe.domain.repository;

import albabe.albabe.domain.entity.JobApplicationEntity;
import albabe.albabe.domain.entity.JobPostEntity;
import albabe.albabe.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplicationEntity, Long> {
    List<JobApplicationEntity> findByJobPost(JobPostEntity jobPost);
    boolean existsByJobPostAndApplicant(JobPostEntity jobPost, UserEntity applicant);

    List<JobApplicationEntity> findByJobPostId(Long jobPostId);
    List<JobApplicationEntity> findAllByJobPost(JobPostEntity jobPost);
}
