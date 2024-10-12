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

    @PostMapping("/apply")
    public ResponseEntity<ApiResponse<JobApplicationEntity>> applyForJob(@RequestParam Long jobPostId, @RequestParam String email, @RequestBody String resume) {
        JobApplicationEntity application = jobApplicationService.applyForJob(jobPostId, email, resume);
        return ResponseEntity.ok(new ApiResponse<>(true, "알바 신청이 완료되었습니다.", application));
    }

    @GetMapping("/job-post/{jobPostId}")
    public ResponseEntity<ApiResponse<List<JobApplicationEntity>>> getApplicationsForJobPost(@PathVariable Long jobPostId, @RequestParam String email) {
        List<JobApplicationEntity> applications = jobApplicationService.getApplicationsForJobPost(jobPostId, email);
        return ResponseEntity.ok(new ApiResponse<>(true, "알바 신청 목록 조회 성공", applications));
    }
}
