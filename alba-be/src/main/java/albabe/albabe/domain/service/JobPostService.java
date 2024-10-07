package albabe.albabe.domain.service;

import albabe.albabe.domain.entity.JobPostEntity;
import albabe.albabe.domain.repository.JobPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobPostService {
    @Autowired
    private JobPostRepository jobPostRepository;

    public void createJobPost(JobPostEntity jobPost) {
        jobPostRepository.save(jobPost);
    }

    public List<JobPostEntity> getAllJobPosts() {
        return jobPostRepository.findAll();
    }
}

