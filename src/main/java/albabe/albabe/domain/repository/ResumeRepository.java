package albabe.albabe.domain.repository;

import albabe.albabe.domain.entity.ResumeEntity;
import albabe.albabe.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestPart;

import java.util.List;

@Repository
public interface ResumeRepository extends JpaRepository<ResumeEntity, Long> {
    List<ResumeEntity> findAllByPersonal(UserEntity personal);
    // 필요한 경우 커스텀 쿼리 메서드 정의 가능
}
