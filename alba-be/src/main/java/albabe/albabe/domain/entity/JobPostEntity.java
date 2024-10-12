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

    private String title;         // 공고 제목
    private String companyName;   // 회사 이름
    private String companyContent;// 회사 주요 사업내용
    private String companyImage;  // 회사 이미지
    private String workCategory;  // 모집 직종
    private String workType;      // 고용 형태
    private int peopleNum;        // 모집 인원
    private String career;        // 경력
    private String workTerm;      // 근무 기간
    private String workDays;      // 근무 요일
    private String workTime;      // 근무 시간
    private String pay;           // 급여
    private String gender;        // 성별
    private String age;           // 나이
    private String deadline;      // 모집 마감일
    private String submitMethod;  // 지원 방법
    private String managerName;   // 담당자 이름
    private String managerEmail;  // 담당자 이메일
    private String managerPhone;  // 담당자 연락처
}
