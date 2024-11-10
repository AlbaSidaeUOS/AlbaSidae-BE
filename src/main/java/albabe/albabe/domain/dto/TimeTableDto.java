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
public class TimeTableDto {
    private Long id;
    private boolean isRegistered;
    private List<Integer> monday;
    private List<Integer> tuesday;
    private List<Integer> wednesday;
    private List<Integer> thursday;
    private List<Integer> friday;
}