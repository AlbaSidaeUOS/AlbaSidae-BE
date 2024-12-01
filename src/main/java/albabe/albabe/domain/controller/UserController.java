package albabe.albabe.domain.controller;

import albabe.albabe.domain.dto.UserDto;
import albabe.albabe.domain.enums.UserRole;
import albabe.albabe.domain.service.UserService;
import albabe.albabe.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(new ApiResponse<>(true, "회원 목록 조회 성공", users));
    }

    // 사용자 정보 조회 (이메일로)
    @GetMapping("/{email}")
    public ResponseEntity<ApiResponse<UserDto>> getUserByEmail(@PathVariable String email) {
        UserDto user = userService.getUserByEmail(email);
        return ResponseEntity.ok(new ApiResponse<>(true, "사용자 조회 성공", user));
    }

    // 사용자 정보 업데이트 (이메일로)
    @PutMapping("/{email}")
    public ResponseEntity<ApiResponse<UserDto>> updateUserByEmail(@PathVariable String email, @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUserByEmail(email, userDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "사용자 정보 업데이트 성공", updatedUser));
    }

    // 사용자 삭제 (이메일로)
    @DeleteMapping("/{email}")
    public ResponseEntity<ApiResponse<Void>> deleteUserByEmail(@PathVariable String email) {
        userService.deleteUserByEmail(email);
        return ResponseEntity.ok(new ApiResponse<>(true, "사용자 삭제 완료", null));
    }

    // 아이디(이메일) 찾기
    @GetMapping("/find-id")
    public ResponseEntity<ApiResponse<String>> findEmail(
            @RequestParam String name,
            @RequestParam String phone,
            @RequestParam UserRole role,
            @RequestParam(required = false) String businessNumber) {

        String email = userService.findEmail(name, phone, role, businessNumber);
        return ResponseEntity.ok(new ApiResponse<>(true, "이메일 찾기 성공", email));
    }

    // 비밀번호 찾기 (재설정 링크 전송 혹은 즉시 재설정 처리)
    @PostMapping("/find-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(
            @RequestParam String email,
            @RequestParam String name,
            @RequestParam String phone,
            @RequestParam UserRole role,
            @RequestParam(required = false) String businessNumber,
            @RequestParam String newPassword) {

        userService.resetPassword(email, name, phone, role, businessNumber, newPassword);
        return ResponseEntity.ok(new ApiResponse<>(true, "비밀번호 재설정 성공", null));
    }

    // 이미지 업로드 메서드
    @PutMapping(value = "/{email}/image")
    public ResponseEntity<ApiResponse<String>> uploadUserImage(
            @PathVariable String email,
            @RequestParam("image") MultipartFile image) {
        String imageUrl = userService.uploadUserImage(email, image);
        return ResponseEntity.ok(new ApiResponse<>(true, "이미지 업로드 성공", imageUrl));
    }
}
