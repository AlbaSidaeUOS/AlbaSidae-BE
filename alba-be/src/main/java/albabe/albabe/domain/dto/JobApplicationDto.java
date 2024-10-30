package albabe.albabe.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationDto {
    private Long id;
    private String resume;
    private String applicantName;  // 신청자의 이름
    private String applicantEmail; // 신청자의 이메일
    private String jobPostTitle;   // 구인 공고의 제목
}