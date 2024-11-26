package albabe.albabe.domain.controller;

import albabe.albabe.domain.dto.JobApplicationDto;
import albabe.albabe.domain.dto.JobPostResponse;
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
    public ResponseEntity<ApiResponse<List<JobApplicationDto>>> getApplicationsForJobPost(
            @PathVariable Long jobPostId) {
        List<JobApplicationDto> applications = jobApplicationService.getApplicationsForJobPost(jobPostId);
        return ResponseEntity.ok(new ApiResponse<>(true, "신청 목록 조회 성공", applications));
    }

    @GetMapping("/applied-jobs")
    public ResponseEntity<ApiResponse<List<JobPostResponse>>> getAppliedJobs(
            @RequestParam("email") String email) {
        List<JobPostResponse> appliedJobs = jobApplicationService.getAppliedJobPosts(email);
        return ResponseEntity.ok(new ApiResponse<>(true, "지원한 공고 목록 조회 성공", appliedJobs));
    }
}
