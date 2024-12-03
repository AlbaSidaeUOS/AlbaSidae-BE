package albabe.albabe.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "job_applications")
public class JobApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_post_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private JobPostEntity jobPost; // 지원한 공고

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id", nullable = false)
    private UserEntity applicant; // 지원자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ResumeEntity resume; // 이력서

    @Column(length = 1023)
    private String description; // 자기소개

    @PostPersist
    public void updateJobPostApplicantCount() {
        if (this.jobPost != null) {
            this.jobPost.incrementApplicantCount(); // 지원자 수 증가
        }
    }
}
