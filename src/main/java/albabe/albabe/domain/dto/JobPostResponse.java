package albabe.albabe.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
    Description : 모집 공고 Dto 소스파일
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobPostResponse {
    private Long id;
    private String title;
    private String companyName;
    private String companyContent;
    private String companyImage; // URL 형태의 이미지 저장
    private String place;
    private String placeDetail;
    private List<String> workCategory;
    private List<String> workType;
    private List<String> workTimeCategory;
    private int peopleNum;
    private String career;
    private String workTerm;
    private List<String> workDays;
    private String workTime;
    private String pay;
    private String gender;
    private String age;
    private String deadline;
    private List<String> submitMethod;
    private CompanyDto company;
    private Date createdAt;

}
