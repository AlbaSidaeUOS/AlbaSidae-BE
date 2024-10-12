package albabe.albabe.domain.service;

import albabe.albabe.domain.entity.JobPostEntity;
import albabe.albabe.domain.entity.UserEntity;
import albabe.albabe.domain.enums.UserRole;
import albabe.albabe.domain.repository.JobPostRepository;
import albabe.albabe.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobPostService {

    @Autowired
    private JobPostRepository jobPostRepository;

    @Autowired
    private UserRepository userRepository;

    // 구인 공고 작성
    public JobPostEntity createJobPost(JobPostEntity jobPost, String email) {
        UserEntity company = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("회사 계정을 찾을 수 없습니다."));

        // 역할 검증
        if (!company.getRole().equals(UserRole.COMPANY)) {
            throw new RuntimeException("해당 권한이 없습니다.");
        }

        jobPost.setCompany(company); // 회사 정보 추가
        return jobPostRepository.save(jobPost);
    }

    // 구인 공고 목록 조회
    public List<JobPostEntity> getAllJobPosts() {
        return jobPostRepository.findAll();
    }

    // 구인 공고 상세 조회
    public JobPostEntity getJobPostById(Long id) {
        return jobPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("구인 공고를 찾을 수 없습니다."));
    }

    // 구인 공고 수정
    public JobPostEntity updateJobPost(Long id, JobPostEntity updatedJobPost, String email) {
        JobPostEntity existingJobPost = getJobPostById(id);

        if (!existingJobPost.getCompany().getEmail().equals(email)) {
            throw new RuntimeException("해당 구인 공고를 수정할 권한이 없습니다.");
        }

        existingJobPost.setTitle(updatedJobPost.getTitle());
        existingJobPost.setDescription(updatedJobPost.getDescription());
        // 필요한 필드들 업데이트

        return jobPostRepository.save(existingJobPost);
    }

    // 구인 공고 삭제
    public void deleteJobPost(Long id, String email) {
        JobPostEntity jobPost = getJobPostById(id);

        if (!jobPost.getCompany().getEmail().equals(email)) {
            throw new RuntimeException("해당 구인 공고를 삭제할 권한이 없습니다.");
        }

        jobPostRepository.deleteById(id);
    }
}
