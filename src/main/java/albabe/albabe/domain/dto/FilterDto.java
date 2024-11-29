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
    private Long id;
    private String email;
    private List<String> workLocations;        // 근무 위치
    private List<String> workCategories;   // 아르바이트 종류
    private List<String> workTerms;    // 근무 기간(1일, 1주일 이하, 1주일-1개월, 1개월-3개월, 3개월-6개월, 6개월-1년, 1년이상)
    private List<String> workDays;        // 근무 요일(평일, 주말, 월, 화, 수, 목, 금, 토, 일)
    private List<String> workTimeCategory;             // 근무 시간 (예: "오전, 오후, 저녁, 새벽")
    public boolean useTimeTable;        // 시간표와의 겹침 제외 여부
}