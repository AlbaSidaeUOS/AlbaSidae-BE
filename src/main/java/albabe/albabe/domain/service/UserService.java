package albabe.albabe.domain.service;

import albabe.albabe.domain.dto.UserDto;
import albabe.albabe.domain.entity.UserEntity;
import albabe.albabe.domain.enums.UserRole;
import albabe.albabe.domain.repository.UserRepository;
import albabe.albabe.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // 이메일 형식 검증을 위한 정규 표현식
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    // 회원가입 처리
    public UserDto registerUser(UserDto userDto, String rawPassword) {
        // 이메일 형식 검증
        if (!EMAIL_PATTERN.matcher(userDto.getEmail()).matches()) {
            throw new IllegalArgumentException("올바르지 않은 이메일 형식입니다.");
        }

        // 이메일 중복 확인
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 비밀번호 길이 확인
        if (rawPassword.length() < 8 || rawPassword.length() > 15) {
            throw new IllegalArgumentException("비밀번호는 8자에서 15자 사이여야 합니다.");
        }

        UserEntity user = new UserEntity();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(userDto.getRole());
        user.setName(userDto.getName());
        user.setBirthDate(userDto.getBirthDate());
        user.setGender(userDto.getGender());
        user.setPhone(userDto.getPhone());
        user.setBusinessNumber(userDto.getBusinessNumber());

        UserEntity savedUser = userRepository.save(user);
        return new UserDto(
                savedUser.getId(),
                savedUser.getEmail(),
                null, // 비밀번호는 반환하지 않음
                savedUser.getName(),
                savedUser.getBirthDate(),
                savedUser.getGender(),
                savedUser.getPhone(),
                savedUser.getBusinessNumber(),
                savedUser.getImage(),
                savedUser.getRole()
        );
    }

    // 로그인 처리
    public Map<String, Object> authenticateUser(String email, String password, UserRole expectedRole) {
        // 1. 이메일 형식 검증
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }

        // 2. 비밀번호 길이 확인
        if (password.length() < 8 || password.length() > 15) {
            throw new IllegalArgumentException("비밀번호는 8자에서 15자 사이여야 합니다.");
        }

        // 3. 이메일 또는 비밀번호가 일치하지 않는 경우
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        // 4. Role 검증
        if (!user.getRole().equals(expectedRole)) {
            throw new IllegalArgumentException("접근 권한이 없습니다. 올바른 권한으로 로그인 해주세요.");
        }

        // JWT 토큰 생성
        String token = jwtTokenProvider.createToken(user.getEmail(), user.getRole().toString());
        String exprTime = "3600"; // 예시로 1시간 만료

        // 응답에 포함할 데이터
        Map<String, Object> loginResponse = new HashMap<>();
        loginResponse.put("token", token);
        loginResponse.put("exprTime", exprTime);
        loginResponse.put("name", user.getName());

        return loginResponse;
    }

    // 이메일로 사용자 정보 조회
    public UserDto getUserByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return new UserDto(
                user.getId(),
                user.getEmail(),
                null,
                user.getName(),
                user.getBirthDate(),
                user.getGender(),
                user.getPhone(),
                user.getBusinessNumber(),
                user.getImage(),
                user.getRole()
        );
    }

    // 이메일로 사용자 정보 업데이트
    public UserDto updateUserByEmail(String email, UserDto userDto) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            if (userDto.getPassword().length() < 8 || userDto.getPassword().length() > 15) {
                throw new IllegalArgumentException("비밀번호는 8자에서 15자 사이여야 합니다.");
            }
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        user.setName(userDto.getName());
        user.setBirthDate(userDto.getBirthDate());
        user.setGender(userDto.getGender());
        user.setPhone(userDto.getPhone());
        user.setBusinessNumber(userDto.getBusinessNumber());

        UserEntity updatedUser = userRepository.save(user);
        return new UserDto(
                updatedUser.getId(),
                updatedUser.getEmail(),
                null,
                updatedUser.getName(),
                updatedUser.getBirthDate(),
                updatedUser.getGender(),
                updatedUser.getPhone(),
                updatedUser.getBusinessNumber(),
                updatedUser.getImage(),
                updatedUser.getRole()
        );
    }

    // 사용자 삭제
    public void deleteUserByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        userRepository.delete(user);
    }
}
