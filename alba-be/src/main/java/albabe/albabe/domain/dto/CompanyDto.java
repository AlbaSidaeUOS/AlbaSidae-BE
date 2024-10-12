package albabe.albabe.domain.dto;

import albabe.albabe.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {
    private Long id;
    private String email;
    private String name;
    private UserRole role;
}
