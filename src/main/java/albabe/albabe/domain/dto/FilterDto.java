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
public class FilterDto {
    private long id;                    // 회원번호
    private List<String> workDays;       // 희망 근무 요일
    private List<String> workCategory;   // 아르바이트 종류
    private List<String> workType;       // 아르바이트 방식
    private String workTime;             // 근무 시간 (예: "14~18")
    private String pay;                  // 급여 유형 (예: "일급", "시급", "월급" 등)
    private String gender;               // 성별 조건 (예: "남", "여", "무관")
    private String age;                  // 나이 조건 (예: "20~25")
    public boolean useTimeTable;        // 시간표와의 겹침 제외 여부
}