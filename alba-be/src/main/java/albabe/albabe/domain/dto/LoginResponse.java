package albabe.albabe.domain.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String exprTime;
    private String username;
}
