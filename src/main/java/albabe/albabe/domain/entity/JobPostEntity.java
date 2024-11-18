package albabe.albabe.domain.entity;

import albabe.albabe.domain.dto.JobPostResponse;
import albabe.albabe.domain.dto.CompanyDto;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private String companyImage; // 이미지 URL을 저장하는 String 타입
    private String place;

    @ElementCollection
    private List<String> workCategory;

    @ElementCollection
    private List<String> workType;

    private int peopleNum;
    private String career;
    private String workTerm;

    @ElementCollection
    private List<String> workDays;

    @ElementCollection
    private List<String> workTimeCategory;

    private String workTime;
    private String pay;
    private String gender;
    private String age;
    private String deadline;

    @ElementCollection
    private List<String> submitMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private UserEntity company;
}
