package albabe.albabe.domain.service;

import albabe.albabe.domain.dto.CompanyDto;
import albabe.albabe.domain.dto.JobPostResponse;
import albabe.albabe.domain.entity.JobPostEntity;
import albabe.albabe.domain.entity.UserEntity;
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

    // 공고 생성 메서드
    public JobPostResponse createJobPost(JobPostEntity jobPost, String email) {
        // 회사 계정 찾기
        UserEntity company = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회사 계정을 찾을 수 없습니다."));

        // 회사 정보 설정
        jobPost.setCompany(company);

        // JobPostEntity 저장
        JobPostEntity savedJobPost = jobPostRepository.save(jobPost);

        // CompanyDto 생성
        CompanyDto companyDto = new CompanyDto(
                company.getId(),
                company.getEmail(),
                company.getName(),
                company.getRole()
        );

        // JobPostResponse 생성 및 반환
        return new JobPostResponse(
                savedJobPost.getId(),
                savedJobPost.getTitle(),
                savedJobPost.getCompanyName(),
                savedJobPost.getCompanyContent(),
                savedJobPost.getCompanyImage(),
                savedJobPost.getWorkCategory(),
                savedJobPost.getWorkType(),
                savedJobPost.getPeopleNum(),
                savedJobPost.getCareer(),
                savedJobPost.getWorkTerm(),
                savedJobPost.getWorkDays(),
                savedJobPost.getWorkTime(),
                savedJobPost.getPay(),
                savedJobPost.getGender(),
                savedJobPost.getAge(),
                savedJobPost.getDeadline(),
                savedJobPost.getSubmitMethod(),
                companyDto
        );
    }

    public List<JobPostEntity> getAllJobPosts() {
        return jobPostRepository.findAll();
    }

    public JobPostEntity getJobPostById(Long id) {
        return jobPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("구인 공고를 찾을 수 없습니다."));
    }

    public JobPostEntity updateJobPost(Long id, JobPostEntity updatedJobPost, String email) {
        JobPostEntity existingJobPost = getJobPostById(id);
        if (!existingJobPost.getCompany().getEmail().equals(email)) {
            throw new IllegalArgumentException("해당 구인 공고를 수정할 권한이 없습니다.");
        }

        existingJobPost.setTitle(updatedJobPost.getTitle());
        existingJobPost.setCompanyName(updatedJobPost.getCompanyName());
        existingJobPost.setCompanyContent(updatedJobPost.getCompanyContent());
        existingJobPost.setCompanyImage(updatedJobPost.getCompanyImage());
        existingJobPost.setWorkCategory(updatedJobPost.getWorkCategory());
        existingJobPost.setWorkType(updatedJobPost.getWorkType());
        existingJobPost.setPeopleNum(updatedJobPost.getPeopleNum());
        existingJobPost.setCareer(updatedJobPost.getCareer());
        existingJobPost.setWorkTerm(updatedJobPost.getWorkTerm());
        existingJobPost.setWorkDays(updatedJobPost.getWorkDays());
        existingJobPost.setWorkTime(updatedJobPost.getWorkTime());
        existingJobPost.setPay(updatedJobPost.getPay());
        existingJobPost.setGender(updatedJobPost.getGender());
        existingJobPost.setAge(updatedJobPost.getAge());
        existingJobPost.setDeadline(updatedJobPost.getDeadline());
        existingJobPost.setSubmitMethod(updatedJobPost.getSubmitMethod());

        return jobPostRepository.save(existingJobPost);
    }

    public void deleteJobPost(Long id, String email) {
        JobPostEntity jobPost = getJobPostById(id);
        if (!jobPost.getCompany().getEmail().equals(email)) {
            throw new IllegalArgumentException("해당 구인 공고를 삭제할 권한이 없습니다.");
        }

        jobPostRepository.delete(jobPost);
    }
}
