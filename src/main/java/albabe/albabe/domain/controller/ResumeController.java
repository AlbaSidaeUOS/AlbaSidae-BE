package albabe.albabe.domain.controller;

import albabe.albabe.domain.dto.JobPostResponse;
import albabe.albabe.domain.dto.ResumeDto;
import albabe.albabe.domain.entity.ResumeEntity;
import albabe.albabe.domain.service.ResumeService;
import albabe.albabe.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @PostMapping
    public ResponseEntity<ApiResponse<ResumeDto>> createResume(@RequestBody ResumeEntity resumeEntity, @RequestParam String email) {
        ResumeDto createdResume = resumeService.createResume(resumeEntity, email);
        return ResponseEntity.ok(new ApiResponse<>(true, "이력서가 등록되었습니다.", createdResume));
    }

    // 이력서 조회 (전체)
//    @GetMapping
//    public ResponseEntity<ApiResponse<List<ResumeDto>>> getAllResumes() {
//        List<ResumeDto> resumes = resumeService.getAllResumes();
//        return ResponseEntity.ok(new ApiResponse<>(true, "이력서 목록 조회 성공", resumes));
//    }

    // 이메일을 기준으로 이력서를 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<ResumeDto>>> getResumes(
            @RequestParam(value = "email", required = false) String email) {
        List<ResumeDto> resumes = resumeService.getResumes(email);
        return ResponseEntity.ok(new ApiResponse<>(true, "이력서 목록 조회 성공", resumes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResumeDto>> getResume(@PathVariable Long id) {
        ResumeDto resume = resumeService.getResumeById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "이력서 조회 성공", resume));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ResumeDto>> updateResume(
            @PathVariable Long id, @RequestBody ResumeEntity updatedResume, @RequestParam String email) {
        ResumeDto updatedResumeDto = resumeService.updateResume(id, updatedResume, email);
        return ResponseEntity.ok(new ApiResponse<>(true, "이력서 수정 완료", updatedResumeDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteResume(@PathVariable Long id, @RequestParam String email) {
            resumeService.deleteResume(id, email);
            return ResponseEntity.ok(new ApiResponse<>(true, "이력서 삭제 완료", null));
    }
}