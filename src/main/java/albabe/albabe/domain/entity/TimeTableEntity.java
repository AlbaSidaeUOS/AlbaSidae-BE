package albabe.albabe.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import albabe.albabe.domain.dto.TimeTableDto;

import java.util.List;

@Entity
@Table(name = "timetable")
@Getter
@Setter
@NoArgsConstructor
public class TimeTableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isRegistered;

    private String monday;
    private String tuesday;
    private String wednesday;
    private String thursday;
    private String friday;
    private String saturday;
    private String sunday;

    public TimeTableDto convertToDto() {
        TimeTableDto dto = new TimeTableDto();
        dto.setEmail(this.email);
        dto.setRegistered(this.isRegistered);
        dto.setMonday(this.monday);
        dto.setTuesday(this.tuesday);
        dto.setWednesday(this.wednesday);
        dto.setThursday(this.thursday);
        dto.setFriday(this.friday);
        dto.setThursday(this.saturday);
        dto.setFriday(this.sunday);
        return dto;
    }
}