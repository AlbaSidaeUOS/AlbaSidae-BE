package albabe.albabe.domain.controller;

import albabe.albabe.domain.entity.JobPostEntity;
import albabe.albabe.domain.service.JobPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobPostController {
    @Autowired
    private JobPostService jobPostService;

    @PostMapping
    @Operation(summary = "구인 공고 생성", description = "새로운 구인 공고를 생성합니다.")
    @ApiResponse(responseCode = "200", description = "구인 공고가 성공적으로 생성되었습니다.")
    public void createJobPost(@RequestBody JobPostEntity jobPost) {
        jobPostService.createJobPost(jobPost);
    }

    @GetMapping
    public List<JobPostEntity> getAllJobPosts() {
        return jobPostService.getAllJobPosts();
    }
}

