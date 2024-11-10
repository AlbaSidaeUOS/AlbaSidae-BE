package albabe.albabe.domain.controller;

import albabe.albabe.domain.dto.TimeTableDto;
import albabe.albabe.domain.entity.TimeTableEntity;
import albabe.albabe.domain.service.TimeTableService;
import albabe.albabe.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timetable")
public class TimeTableController {

    @Autowired
    private TimeTableService timeTableService;

    // 시간표 최초 등록
    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse<TimeTableDto>> registerTimeTable(@RequestBody TimeTableDto timeTableDto, @PathVariable Long userId) {
        TimeTableDto registeredTimeTableDto = timeTableService.registerTimeTable(timeTableDto, userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "시간표가 등록되었습니다.", registeredTimeTableDto));
    }

    // 시간표 조회
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<TimeTableDto>> getTimeTable(@PathVariable Long userId) {
        TimeTableDto timeTableDto = timeTableService.getTimeTable(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "시간표 조회 성공.", timeTableDto));
    }

    // 시간표 수정
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<TimeTableDto>> updateTimeTable(
            @PathVariable Long userId, @RequestBody TimeTableDto timeTableDto) {
        TimeTableDto updatedTimeTableDto = timeTableService.updateTimeTable(timeTableDto, userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "시간표 수정 성공.", updatedTimeTableDto));
    }
}