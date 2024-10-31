package albabe.albabe.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "job_posts")
public class JobPostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String companyName;
    private String companyContent;
    private String companyImage;
    private String workCategory;
    private String workType;
    private int peopleNum;
    private String career;
    private String workTerm;
    private String workDays;
    private String workTime;
    private String pay;
    private String gender;
    private String age;
    private String deadline;
    private String submitMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private UserEntity company;
}
