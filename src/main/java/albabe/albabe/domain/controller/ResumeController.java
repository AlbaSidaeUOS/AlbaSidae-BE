package albabe.albabe.domain.controller;

import albabe.albabe.domain.dto.ResumeDto;
import albabe.albabe.domain.entity.ResumeEntity;
import albabe.albabe.domain.service.ResumeService;
import albabe.albabe.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @PostMapping
    public ResponseEntity<ApiResponse<ResumeDto>> createResume(@RequestBody ResumeEntity resumeEntity, @RequestParam String email) {
        ResumeDto createdResume = resumeService.createResume(resumeEntity, email);
        return ResponseEntity.ok(new ApiResponse<>(true, "구인 공고가 등록되었습니다.", createdResume));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResumeDto>> getResume(@PathVariable Long id) {
        ResumeDto resume = resumeService.getResumeById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "구인 공고 목록 조회 성공", resume));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ResumeDto>> updateResume(
            @PathVariable Long id, @RequestBody ResumeEntity updatedResume, @RequestParam String email) {
        ResumeDto updatedResumeDto = resumeService.updateResume(id, updatedResume, email);
        return ResponseEntity.ok(new ApiResponse<>(true, "이력서 업데이트 완료", updatedResumeDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteResume(@PathVariable Long id, @RequestParam String email) {
            resumeService.deleteResume(id, email);
            return ResponseEntity.ok(new ApiResponse<>(true, "이력서 삭제 완료", null));
    }
}