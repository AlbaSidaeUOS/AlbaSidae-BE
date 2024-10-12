package albabe.albabe.domain.repository;

import albabe.albabe.domain.entity.JobPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostRepository extends JpaRepository<JobPostEntity, Long> {
}
