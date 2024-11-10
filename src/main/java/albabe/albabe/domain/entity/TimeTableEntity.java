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

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isRegistered;

    @ElementCollection
    private List<Integer> monday;

    @ElementCollection
    private List<Integer> tuesday;

    @ElementCollection
    private List<Integer> wednesday;

    @ElementCollection
    private List<Integer> thursday;

    @ElementCollection
    private List<Integer> friday;

    public TimeTableDto convertToDto() {
        TimeTableDto dto = new TimeTableDto();
        dto.setId(this.id);
        dto.setId(this.user.getId());
        dto.setRegistered(this.isRegistered);
        dto.setMonday(this.monday);
        dto.setTuesday(this.tuesday);
        dto.setWednesday(this.wednesday);
        dto.setThursday(this.thursday);
        dto.setFriday(this.friday);
        return dto;
    }
}