package albabe.albabe.domain.repository;

import albabe.albabe.domain.entity.TimeTableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TimeTableRepository extends JpaRepository<TimeTableEntity, Long> {
    Optional<TimeTableEntity> findByUserId(Long userId);
}