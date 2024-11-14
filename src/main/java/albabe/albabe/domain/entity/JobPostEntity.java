package albabe.albabe.domain.entity;

import albabe.albabe.domain.dto.JobPostResponse;
import albabe.albabe.domain.dto.CompanyDto;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

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

    public JobPostResponse toResponse(JobPostEntity jobPost) {
        JobPostResponse response = new JobPostResponse();
        response.setId(jobPost.getId());
        response.setTitle(jobPost.getTitle());
        response.setCompanyName(jobPost.getCompanyName());
        response.setCompanyContent(jobPost.getCompanyContent());
        response.setCompanyImage(jobPost.getCompanyImage());
        response.setPlace(jobPost.getPlace());
        response.setWorkCategory(jobPost.getWorkCategory());
        response.setWorkType(jobPost.getWorkType());
        response.setPeopleNum(jobPost.getPeopleNum());
        response.setCareer(jobPost.getCareer());
        response.setWorkTerm(jobPost.getWorkTerm());
        response.setWorkDays(jobPost.getWorkDays());
        response.setWorkTime(jobPost.getWorkTime());
        response.setPay(jobPost.getPay());
        response.setGender(jobPost.getGender());
        response.setAge(jobPost.getAge());
        response.setDeadline(jobPost.getDeadline());
        response.setSubmitMethod(jobPost.getSubmitMethod());

        return response;
    }
}
