package albabe.albabe.domain.entity;

import albabe.albabe.domain.dto.JobPostResponse;
import albabe.albabe.domain.dto.CompanyDto;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
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
    private String placeDetail;

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
    private List<String> workTimeCategory = new ArrayList<>();

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

    private int applicantCount = 0; // 지원자 수 초기값

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createdAt; // 등록 시간

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date(); // 등록 시간 자동 설정
    }

    // 지원자 수 증가 메서드
    public void incrementApplicantCount() {
        this.applicantCount++;
    }

    // 엔티티가 로드되거나 workTime이 설정될 때 workTimeCategory를 채우는 유틸리티 메서드
    public void updateWorkTimeCategory() {
        if (workTime == null || workTime.isBlank()) return;

        workTimeCategory.clear(); // 기존 값을 비우고
        if (workTime.equals("any")) {
            workTimeCategory.add("시간협의");
            return;
        }

        String[] timeRange = workTime.split("~");
        int start = Integer.parseInt(timeRange[0]); // 시작 시간
        int end = Integer.parseInt(timeRange[1]);   // 종료 시간

        int currentHour = start;
        while (true) {
            if (currentHour == end) break;
            if (currentHour == 24)
                currentHour = 0;
            if (currentHour >= 6 && currentHour < 12) {
                if (!workTimeCategory.contains("오전")) workTimeCategory.add("오전");
            } else if (currentHour >= 12 && currentHour < 18) {
                if (!workTimeCategory.contains("오후")) workTimeCategory.add("오후");
            } else if (currentHour >= 18 && currentHour < 24) {
                if (!workTimeCategory.contains("저녁")) workTimeCategory.add("저녁");
            } else if (currentHour >= 0 && currentHour < 6) {
                if (!workTimeCategory.contains("새벽")) workTimeCategory.add("새벽");
            }
            currentHour++;
        }
    }

    // setter를 통해 workTime이 설정되면 workTimeCategory를 업데이트
    public void setWorkTime(String workTime) {
        this.workTime = workTime;
        updateWorkTimeCategory();
    }
}
