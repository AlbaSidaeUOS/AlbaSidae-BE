package albabe.albabe.domain.controller;

import albabe.albabe.domain.dto.TimeTableDto;
import albabe.albabe.domain.entity.TimeTableEntity;
import albabe.albabe.domain.service.TimeTableService;
import albabe.albabe.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
    Description : 시간표 등록과 관련된 컨트롤러 소스파일
 */

@RestController
@RequestMapping("/api/timetable")
public class TimeTableController {

    @Autowired
    private TimeTableService timeTableService;

    // 시간표 최초 등록
    @PostMapping("/{email}")
    public ResponseEntity<ApiResponse<TimeTableDto>> registerTimeTable(@RequestBody TimeTableDto timeTableDto, @PathVariable String email) {
        TimeTableDto registeredTimeTableDto = timeTableService.registerTimeTable(timeTableDto, email);
        return ResponseEntity.ok(new ApiResponse<>(true, "시간표가 등록되었습니다.", registeredTimeTableDto));
    }

    // 시간표 조회
    @GetMapping("/{email}")
    public ResponseEntity<ApiResponse<TimeTableDto>> getTimeTable(@PathVariable String email) {
        TimeTableDto timeTableDto = timeTableService.getTimeTable(email);
        return ResponseEntity.ok(new ApiResponse<>(true, "시간표 조회 성공.", timeTableDto));
    }

    // 시간표 수정
    @PutMapping("/{email}")
    public ResponseEntity<ApiResponse<TimeTableDto>> updateTimeTable(
            @PathVariable String email, @RequestBody TimeTableDto timeTableDto) {
        TimeTableDto updatedTimeTableDto = timeTableService.updateTimeTable(timeTableDto, email);
        return ResponseEntity.ok(new ApiResponse<>(true, "시간표 수정 성공.", updatedTimeTableDto));
    }
}