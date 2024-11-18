package albabe.albabe.domain.service;

import albabe.albabe.domain.dto.TimeTableDto;
import albabe.albabe.domain.entity.TimeTableEntity;
import albabe.albabe.domain.entity.UserEntity;
import albabe.albabe.domain.repository.TimeTableRepository;
import albabe.albabe.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeTableService {

    private final TimeTableRepository timeTableRepository;
    private final UserRepository userRepository;

    @Autowired
    public TimeTableService(TimeTableRepository timeTableRepository, UserRepository userRepository) {
        this.timeTableRepository = timeTableRepository;
        this.userRepository = userRepository;
    }

    // 시간표 등록
    public TimeTableDto registerTimeTable(TimeTableDto timeTableDto, Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        TimeTableEntity timeTable = new TimeTableEntity();
        timeTable.setUser(user);
        timeTable.setRegistered(timeTableDto.isRegistered());
        timeTable.setMonday(timeTableDto.getMonday());
        timeTable.setTuesday(timeTableDto.getTuesday());
        timeTable.setWednesday(timeTableDto.getWednesday());
        timeTable.setThursday(timeTableDto.getThursday());
        timeTable.setFriday(timeTableDto.getFriday());

        TimeTableEntity savedTimeTable = timeTableRepository.save(timeTable);
        return savedTimeTable.convertToDto();
    }

    // 시간표 조회
    public TimeTableDto getTimeTable(Long userId) {
        TimeTableEntity timeTable = timeTableRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("TimeTable not found for User ID: " + userId));

        return timeTable.convertToDto();
    }

    // 시간표 수정
    public TimeTableDto updateTimeTable(TimeTableDto updatedTimeTableDto, Long userId) {
        TimeTableEntity existingTimeTable = timeTableRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("TimeTable not found for User ID: " + userId));

        existingTimeTable.setRegistered(updatedTimeTableDto.isRegistered());
        existingTimeTable.setMonday(updatedTimeTableDto.getMonday());
        existingTimeTable.setTuesday(updatedTimeTableDto.getTuesday());
        existingTimeTable.setWednesday(updatedTimeTableDto.getWednesday());
        existingTimeTable.setThursday(updatedTimeTableDto.getThursday());
        existingTimeTable.setFriday(updatedTimeTableDto.getFriday());

        TimeTableEntity savedTimeTable = timeTableRepository.save(existingTimeTable);
        return savedTimeTable.convertToDto();
    }
}
