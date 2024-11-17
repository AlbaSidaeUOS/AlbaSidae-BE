package albabe.albabe.domain.repository;

import albabe.albabe.domain.entity.JobPostEntity;
import albabe.albabe.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostRepository extends JpaRepository<JobPostEntity, Long> {
    List<JobPostEntity> findByCompany(UserEntity company);
    List<JobPostEntity> findTop12ByOrderByCreatedAtDesc();
    List<JobPostEntity> findTop12ByOrderByApplicantCountDesc();
}
