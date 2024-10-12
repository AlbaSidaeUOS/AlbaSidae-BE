package albabe.albabe.domain.repository;

import albabe.albabe.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // 이메일로 사용자 찾기
    Optional<UserEntity> findByEmail(String email);

    // 이메일 중복 확인
    boolean existsByEmail(String email);
}
