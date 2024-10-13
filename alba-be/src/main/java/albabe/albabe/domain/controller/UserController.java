package albabe.albabe.domain.controller;

import albabe.albabe.domain.dto.UserDto;
import albabe.albabe.domain.service.UserService;
import albabe.albabe.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDto>> registerUser(@RequestBody UserDto userDto) {
        UserDto registeredUser = userService.registerUser(userDto, userDto.getPassword());
        return ResponseEntity.ok(new ApiResponse<>(true, "회원가입이 완료되었습니다.", registeredUser));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> loginUser(@RequestBody UserDto userDto) {
        try {
            // 로그인 성공 시 토큰과 사용자 정보 반환
            var loginResponse = userService.authenticateUser(userDto.getEmail(), userDto.getPassword(), userDto.getRole());
            return ResponseEntity.ok(new ApiResponse<>(true, "로그인 성공", loginResponse));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // 회원 정보 조회
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDto>> getUserInfo(@PathVariable Long userId) {
        UserDto userDto = userService.getUserById(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "회원 정보 조회 성공", userDto));
    }

    // 회원 정보 수정
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDto>> updateUserInfo(@PathVariable Long userId, @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(userId, userDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "회원 정보 수정 성공", updatedUser));
    }

    // 회원 탈퇴
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "회원 탈퇴 성공", null));
    }
}
