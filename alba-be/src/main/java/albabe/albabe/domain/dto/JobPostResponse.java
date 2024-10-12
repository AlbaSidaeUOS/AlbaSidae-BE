package albabe.albabe.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobPostResponse {
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
    private CompanyDto company;
}
