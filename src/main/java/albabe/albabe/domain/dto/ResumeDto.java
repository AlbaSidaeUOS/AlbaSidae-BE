package albabe.albabe.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResumeDto {

    private String applicantName;

    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "연락처 형식이 올바르지 않습니다. xxx-xxxx-xxxx 형식이어야 합니다.")
    private String contactInfo;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    private String address;
    private String resumeTitle;
    private String selfIntroduction;
}
