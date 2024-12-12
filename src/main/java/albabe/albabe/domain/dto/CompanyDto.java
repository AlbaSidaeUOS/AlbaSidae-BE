package albabe.albabe.domain.dto;

import albabe.albabe.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
    Description : 기업정보 Dto 소스파일
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {
    private Long id;
    private String email;
    private String name;
    private String image;
    private UserRole role;
}
