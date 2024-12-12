package albabe.albabe.domain.repository;

import albabe.albabe.domain.entity.JobApplicationEntity;
import albabe.albabe.domain.entity.JobPostEntity;
import albabe.albabe.domain.entity.ResumeEntity;
import albabe.albabe.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/*
    Description : 알바 지원 Repository 소스파일. 모집 공고 찾기, 삭제 함수 존재
 */

public interface JobApplicationRepository extends JpaRepository<JobApplicationEntity, Long> {
    List<JobApplicationEntity> findByJobPost(JobPostEntity jobPost);
    boolean existsByJobPostAndApplicant(JobPostEntity jobPost, UserEntity applicant);

    List<JobApplicationEntity> findByJobPostId(Long jobPostId);
    List<JobApplicationEntity> findAllByJobPost(JobPostEntity jobPost);

    @Query("SELECT app.jobPost FROM JobApplicationEntity app WHERE app.applicant.email = :email")
    List<JobPostEntity> findJobPostsByUserEmail(@Param("email") String email);

    void deleteByJobPost(JobPostEntity jobPost);
    void deleteByResume(ResumeEntity resume);

}
