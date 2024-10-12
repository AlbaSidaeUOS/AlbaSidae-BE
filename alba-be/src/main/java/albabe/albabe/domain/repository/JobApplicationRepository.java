package albabe.albabe.domain.repository;

import albabe.albabe.domain.entity.JobApplicationEntity;
import albabe.albabe.domain.entity.JobPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplicationEntity, Long> {
    // 특정 구인 공고에 대한 알바 신청 목록 조회
    List<JobApplicationEntity> findByJobPost(JobPostEntity jobPost);
}
