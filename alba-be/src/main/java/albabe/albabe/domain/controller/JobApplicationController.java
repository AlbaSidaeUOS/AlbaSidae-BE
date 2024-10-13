package albabe.albabe.domain.controller;

import albabe.albabe.domain.entity.JobApplicationEntity;
import albabe.albabe.domain.service.JobApplicationService;
import albabe.albabe.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-applications")
public class JobApplicationController {

    @Autowired
    private JobApplicationService jobApplicationService;

    // 알바 지원
    @PostMapping("/apply/{jobPostId}")
    public ResponseEntity<ApiResponse<String>> applyForJob(
            @PathVariable Long jobPostId, @RequestParam String email) {
        jobApplicationService.applyForJob(jobPostId, email);
        return ResponseEntity.ok(new ApiResponse<>(true, "알바 지원이 완료되었습니다.", null));
    }

    // 신청 목록 보기
    @GetMapping("/applications/{jobPostId}")
    public ResponseEntity<ApiResponse<List<JobApplicationEntity>>> getApplicationsForJobPost(
            @PathVariable Long jobPostId) {
        List<JobApplicationEntity> applications = jobApplicationService.getApplicationsForJobPost(jobPostId);
        return ResponseEntity.ok(new ApiResponse<>(true, "신청 목록 조회 성공", applications));
    }
}
