package albabe.albabe.domain.dto;

import albabe.albabe.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String email;
    private String password;
    private String name;
    private String birthDate;
    private String gender;
    private String phone;
    private String businessNumber;
    private UserRole role;
}
