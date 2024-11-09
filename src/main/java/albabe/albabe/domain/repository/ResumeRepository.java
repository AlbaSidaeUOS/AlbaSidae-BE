package albabe.albabe.domain.repository;

import albabe.albabe.domain.entity.ResumeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<ResumeEntity, Long> {
    // 필요한 경우 커스텀 쿼리 메서드 정의 가능
}
