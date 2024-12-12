package albabe.albabe.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/*
    Description : 시간표 Dto 소스파일
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeTableDto {
    private String email;
    private boolean isRegistered;
    private String monday;
    private String tuesday;
    private String wednesday;
    private String thursday;
    private String friday;
    private String saturday;
    private String sunday;
}