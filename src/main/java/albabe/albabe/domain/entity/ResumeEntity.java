package albabe.albabe.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "resumes")
public class ResumeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String resumeTitle;
    private String selfIntroduction;

    // New Fields for Education and Preferences
    private String educationLevel; // 최종학력
    private String preferredWorkLocation; // 희망근무지

    @ElementCollection
    private List<String> preferredJobTypes; // 희망업직종

    @ElementCollection
    private List<String> employmentTypes; // 근무형태

    @ElementCollection
    private List<String> workPeriod; // 근무기간

    @ElementCollection
    private List<String> workDays; // 근무요일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public ResumeEntity() {

    }
}