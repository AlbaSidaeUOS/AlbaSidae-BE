package albabe.albabe.domain.controller;

import albabe.albabe.domain.dto.UserDto;
import albabe.albabe.domain.dto.LoginRequest;
import albabe.albabe.domain.dto.LoginResponse;
import albabe.albabe.domain.service.UserService;
import albabe.albabe.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.AuthenticationException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDto>> registerUser(@RequestBody UserDto userDto) {
        UserDto registeredUser = userService.registerUser(userDto, userDto.getPassword());
        return ResponseEntity.ok(new ApiResponse<>(true, "회원가입이 완료되었습니다.", registeredUser));
    }

    @PostMapping("/signIn")
    public ResponseEntity<ApiResponse<LoginResponse>> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = userService.authenticateUser(loginRequest);
            return ResponseEntity.ok(new ApiResponse<>(true, "로그인 성공", response));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "아이디 또는 비밀번호가 잘못되었습니다.", null));
        }
    }
}
