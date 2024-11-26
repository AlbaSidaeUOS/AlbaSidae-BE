package albabe.albabe.domain.repository;

import albabe.albabe.domain.entity.JobPostEntity;
import albabe.albabe.domain.entity.ResumeEntity;
import albabe.albabe.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestPart;

import java.util.List;

@Repository
public interface ResumeRepository extends JpaRepository<ResumeEntity, Long> {
    // 이메일이 제공되지 않을 경우 전체 조회
    @Query("SELECT r FROM ResumeEntity r WHERE :email IS NULL OR r.personal.email = :email")
    List<ResumeEntity> findResumesByEmail(@Param("email") String email);

    List<ResumeEntity> findAllByPersonal(UserEntity personal);

    // 필요한 경우 커스텀 쿼리 메서드 정의 가능
}
