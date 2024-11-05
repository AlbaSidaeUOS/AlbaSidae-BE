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
    public ResponseEntity<ApiResponse<ResumeEntity>> createResume(@RequestBody ResumeDto resumeDto) {
        try {
            ResumeEntity createdResume = resumeService.createResume(resumeDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "이력서 등록이 완료되었습니다.", createdResume));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResumeEntity>> getResume(@PathVariable Long id) {
        try {
            ResumeEntity resume = resumeService.getResumeById(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "이력서 조회 성공", resume));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ResumeEntity>> updateResume(@PathVariable Long id, @RequestBody ResumeDto resumeDto) {
        try {
            ResumeEntity updatedResume = resumeService.updateResume(id, resumeDto);
            return ResponseEntity.ok(new ApiResponse<>(true, "이력서 업데이트 완료", updatedResume));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteResume(@PathVariable Long id) {
        try {
            resumeService.deleteResume(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "이력서 삭제 완료", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
