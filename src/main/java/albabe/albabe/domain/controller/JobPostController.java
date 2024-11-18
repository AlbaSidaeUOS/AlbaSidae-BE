package albabe.albabe.domain.controller;

import albabe.albabe.domain.dto.JobPostResponse;
import albabe.albabe.domain.dto.FilterDto;
import albabe.albabe.domain.entity.JobPostEntity;
import albabe.albabe.domain.service.JobPostService;
import albabe.albabe.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/job-posts")
public class JobPostController {

    @Autowired
    private JobPostService jobPostService;

    // 구인 공고 생성
    @PostMapping
    public ResponseEntity<ApiResponse<JobPostResponse>> createJobPost(
            @RequestPart JobPostEntity jobPost,
            @RequestPart(value = "companyImage", required = false) MultipartFile companyImage,
            @RequestParam String email) {
        JobPostResponse createdPost = jobPostService.createJobPost(jobPost, companyImage, email);
        return ResponseEntity.ok(new ApiResponse<>(true, "구인 공고가 등록되었습니다.", createdPost));
    }

    // 구인 공고 조회 (전체)
    @GetMapping
    public ResponseEntity<ApiResponse<List<JobPostResponse>>> getAllJobPosts() {
        List<JobPostResponse> jobPosts = jobPostService.getAllJobPosts();
        return ResponseEntity.ok(new ApiResponse<>(true, "구인 공고 목록 조회 성공", jobPosts));
    }

    // 구인 공고 조회 (단일)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobPostResponse>> getJobPostById(@PathVariable Long id) {
        JobPostResponse jobPost = jobPostService.getJobPostById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "구인 공고 조회 성공", jobPost));
    }

    // 구인 공고 수정
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<JobPostResponse>> updateJobPost(
            @PathVariable Long id,
            @RequestPart JobPostEntity updatedJobPost,
            @RequestPart(value = "updatedCompanyImage", required = false) MultipartFile updatedCompanyImage,
            @RequestParam String email) {
        JobPostResponse updatedPost = jobPostService.updateJobPost(id, updatedJobPost, updatedCompanyImage, email);
        return ResponseEntity.ok(new ApiResponse<>(true, "구인 공고가 수정되었습니다.", updatedPost));
    }

    // 구인 공고 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteJobPost(@PathVariable Long id, @RequestParam String email) {
        jobPostService.deleteJobPost(id, email);
        return ResponseEntity.ok(new ApiResponse<>(true, "구인 공고가 삭제되었습니다.", null));
    }

    // 정렬된 공고 조회 (상위 12개)
    @GetMapping("/sorted")
    public ResponseEntity<ApiResponse<List<JobPostResponse>>> getSortedJobPosts(@RequestParam String sort) {
        List<JobPostResponse> jobPosts;
        if ("latest".equals(sort)) {
            jobPosts = jobPostService.getJobsSortedByLatest();
        } else if ("popular".equals(sort)) {
            jobPosts = jobPostService.getJobsSortedByPopular();
        } else {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "잘못된 정렬 요청입니다.", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "정렬된 구인 공고 조회 성공", jobPosts));
    }
    
    //필터링
    @PostMapping("/filter")
    public ResponseEntity<ApiResponse<List<JobPostResponse>>> getFilteredJobPosts(
            @RequestBody FilterDto filteringDto) {
        List<JobPostResponse> filteredPosts = jobPostService.getFilteredJobPosts(filteringDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "필터링된 구인 공고 조회 성공", filteredPosts));
    }
}
