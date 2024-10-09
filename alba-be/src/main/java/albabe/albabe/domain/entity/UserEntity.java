package albabe.albabe.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String role; // "USER" 또는 "COMPANY"
    private String name; // 개인회원의 이름
    private String birthDate; // 개인회원의 생년월일
    private String email;
    private String phone;
    private String businessNumber; // 기업회원의 사업자번호
}
