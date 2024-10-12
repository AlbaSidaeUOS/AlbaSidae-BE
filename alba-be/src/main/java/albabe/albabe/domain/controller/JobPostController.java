package albabe.albabe.domain.controller;

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

    // 구인 공고 작성 (COMPANY만 가능)
    @PostMapping
    public ResponseEntity<ApiResponse<JobPostEntity>> createJobPost(@RequestBody JobPostEntity jobPost, @RequestParam String email) {
        JobPostEntity createdPost = jobPostService.createJobPost(jobPost, email);
        return ResponseEntity.ok(new ApiResponse<>(true, "구인 공고가 등록되었습니다.", createdPost));
    }

    // 모든 구인 공고 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<JobPostEntity>>> getAllJobPosts() {
        List<JobPostEntity> posts = jobPostService.getAllJobPosts();
        return ResponseEntity.ok(new ApiResponse<>(true, "구인 공고 목록", posts));
    }

    // 특정 구인 공고 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobPostEntity>> getJobPostById(@PathVariable Long id) {
        JobPostEntity post = jobPostService.getJobPostById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "구인 공고 상세 정보", post));
    }

    // 구인 공고 수정 (COMPANY만 가능)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<JobPostEntity>> updateJobPost(@PathVariable Long id, @RequestBody JobPostEntity jobPost, @RequestParam String email) {
        JobPostEntity updatedPost = jobPostService.updateJobPost(id, jobPost, email);
        return ResponseEntity.ok(new ApiResponse<>(true, "구인 공고가 수정되었습니다.", updatedPost));
    }

    // 구인 공고 삭제 (COMPANY만 가능)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteJobPost(@PathVariable Long id, @RequestParam String email) {
        jobPostService.deleteJobPost(id, email);
        return ResponseEntity.ok(new ApiResponse<>(true, "구인 공고가 삭제되었습니다.", null));
    }
}
