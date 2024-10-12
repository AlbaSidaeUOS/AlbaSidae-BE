package albabe.albabe.domain.controller;

import albabe.albabe.domain.dto.JobPostResponse;
import albabe.albabe.domain.entity.JobPostEntity;
import albabe.albabe.domain.service.JobPostService;
import albabe.albabe.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-posts")
public class JobPostController {

    @Autowired
    private JobPostService jobPostService;

    @PostMapping
    public ResponseEntity<ApiResponse<JobPostResponse>> createJobPost(@RequestBody JobPostEntity jobPost, @RequestParam String email) {
        JobPostResponse createdPost = jobPostService.createJobPost(jobPost, email);
        return ResponseEntity.ok(new ApiResponse<>(true, "구인 공고가 등록되었습니다.", createdPost));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobPostEntity>>> getAllJobPosts() {
        List<JobPostEntity> posts = jobPostService.getAllJobPosts();
        return ResponseEntity.ok(new ApiResponse<>(true, "구인 공고 목록", posts));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<JobPostEntity>> updateJobPost(@PathVariable Long id, @RequestBody JobPostEntity jobPost, @RequestParam String email) {
        JobPostEntity updatedPost = jobPostService.updateJobPost(id, jobPost, email);
        return ResponseEntity.ok(new ApiResponse<>(true, "구인 공고가 수정되었습니다.", updatedPost));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteJobPost(@PathVariable Long id, @RequestParam String email) {
        jobPostService.deleteJobPost(id, email);
        return ResponseEntity.ok(new ApiResponse<>(true, "구인 공고가 삭제되었습니다.", null));
    }
}
