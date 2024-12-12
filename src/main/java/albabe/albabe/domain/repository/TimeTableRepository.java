package albabe.albabe.domain.repository;

import albabe.albabe.domain.entity.TimeTableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/*
    Description : 시간표 Repository 소스파일. 시간표 찾기 함수 존재
 */

public interface TimeTableRepository extends JpaRepository<TimeTableEntity, Long> {
    Optional<TimeTableEntity> findByEmail(String email);
}