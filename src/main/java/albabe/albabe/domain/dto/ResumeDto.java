package albabe.albabe.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResumeDto {
    private Long id;
    private String resumeTitle;
    private String selfIntroduction;

    // New Fields for Education and Preferences
    private String educationLevel; // 최종학력 (e.g., 고등학교, 대학교 등)
    private String career; // 경력
    private List<String> preferredWorkLocation; // 희망근무지 (e.g., 서울)
    private List<String> preferredJobTypes; // 희망업직종 (e.g., 의식, 음료, 유통)
    private List<String> employmentTypes; // 근무형태 (e.g., 아르바이트, 계약직)
    private String workPeriod; // 근무기간 (e.g., 3개월 이하, 3개월~6개월)
    private String workDays; // 근무요일 (e.g., 평일, 주말, 요일무관)
    private PersonalDto personal;
}