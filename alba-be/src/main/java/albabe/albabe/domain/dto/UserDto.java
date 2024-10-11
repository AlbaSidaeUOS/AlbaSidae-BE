package albabe.albabe.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String birthDate;
    private String email;
    private String phone;
    private String role;
    private String businessNumber;

    // 필요한 필드를 위한 생성자
    public UserDto(Long id, String username, String name, String birthDate, String email, String phone, String role, String businessNumber) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.birthDate = birthDate;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.businessNumber = businessNumber;
    }
}
