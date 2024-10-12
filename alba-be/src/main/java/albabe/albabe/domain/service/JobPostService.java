package albabe.albabe.domain.service;

import albabe.albabe.domain.entity.JobPostEntity;
import albabe.albabe.domain.repository.JobPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobPostService {

    @Autowired
    private JobPostRepository jobPostRepository;

    // 구인 공고 등록
    public JobPostEntity createJobPost(JobPostEntity jobPost) {
        return jobPostRepository.save(jobPost);
    }
}
