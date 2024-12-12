package albabe.albabe.domain.repository;

import albabe.albabe.domain.entity.UserEntity;
import albabe.albabe.domain.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*
    Description : 회원 Repository 소스파일. 사용자 찾기 함수 존재
 */

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // 이메일로 사용자 찾기
    Optional<UserEntity> findByEmail(String email);

    // 이메일 중복 확인
    boolean existsByEmail(String email);

    // PERSONAL 회원: 이름, 전화번호, 역할로 사용자 찾기
    Optional<UserEntity> findByNameAndPhoneAndRole(String name, String phone, UserRole role);

    // COMPANY 회원: 이름, 전화번호, 역할, 사업자번호로 사용자 찾기
    Optional<UserEntity> findByNameAndPhoneAndRoleAndBusinessNumber(String name, String phone, UserRole role, String businessNumber);

    // PERSONAL 회원: 이메일, 이름, 전화번호, 역할로 사용자 찾기 (비밀번호 찾기)
    Optional<UserEntity> findByEmailAndNameAndPhoneAndRole(String email, String name, String phone, UserRole role);

    // COMPANY 회원: 이메일, 이름, 전화번호, 역할, 사업자번호로 사용자 찾기 (비밀번호 찾기)
    Optional<UserEntity> findByEmailAndNameAndPhoneAndRoleAndBusinessNumber(String email, String name, String phone, UserRole role, String businessNumber);
}
