package albabe.albabe.domain.controller;

import albabe.albabe.domain.dto.ApiResponse;
import albabe.albabe.domain.entity.UserEntity;
import albabe.albabe.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
//@CrossOrigin(origins = "http://localhost:3000")  // 리액트 개발 서버 주소
public class UserController {

    @Autowired
    private UserService userService;

    // 회원가입 API
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserEntity>> registerUser(@RequestBody UserEntity user) {
        try {
            // 사용자명 중복 체크
//            if (userService.existsByUsername(user.getUsername())) {
//                return new ResponseEntity<>(
//                        new ApiResponse<>(false, "이미 존재하는 사용자명입니다."),
//                        HttpStatus.BAD_REQUEST
//                );
//            }

            // 회원 등록
            UserEntity registeredUser = userService.registerUser(
                    user.getUsername(),
                    user.getPassword(),
                    user.getRole(),
                    user.getName(),
                    user.getBirthDate(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getBusinessNumber()
            );

            // 성공 응답 - 등록된 사용자 ID를 포함
            return new ResponseEntity<>(
                    new ApiResponse<>(true, "회원가입이 완료되었습니다.", registeredUser),
                    HttpStatus.OK
            );

        } catch (Exception e) {
            // 에러 응답
            return new ResponseEntity<>(
                    new ApiResponse<>(false, "서버 에러가 발생했습니다: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

}
