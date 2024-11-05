package albabe.albabe.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "resumes")
public class ResumeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String applicantName;
    private String contactInfo;
    private String email;
    private String address;
    private String resumeTitle;
    private String selfIntroduction;

    public ResumeEntity(String applicantName, String contactInfo, String email, String address, String resumeTitle, String selfIntroduction) {
        this.applicantName = applicantName;
        this.contactInfo = contactInfo;
        this.email = email;
        this.address = address;
        this.resumeTitle = resumeTitle;
        this.selfIntroduction = selfIntroduction;
    }

    public ResumeEntity() {

    }
}