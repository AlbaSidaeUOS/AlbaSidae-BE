package albabe.albabe.domain.service;

import albabe.albabe.domain.dto.LoginRequest;
import albabe.albabe.domain.dto.LoginResponse;
import albabe.albabe.domain.dto.UserDto;
import albabe.albabe.domain.entity.UserEntity;
import albabe.albabe.domain.repository.UserRepository;
import albabe.albabe.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserDto registerUser(UserDto userDto, String rawPassword) {
        UserEntity user = new UserEntity();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(userDto.getRole());
        user.setName(userDto.getName());
        user.setBirthDate(userDto.getBirthDate());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setBusinessNumber(userDto.getBusinessNumber());

        UserEntity savedUser = userRepository.save(user);
        return new UserDto(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getName(),
                savedUser.getBirthDate(),
                savedUser.getEmail(),
                savedUser.getPhone(),
                savedUser.getRole(),
                savedUser.getBusinessNumber() // 추가된 부분
        );
    }

    public LoginResponse authenticateUser(LoginRequest loginRequest) {
        UserEntity user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(user.getUsername(), user.getRole());
        return new LoginResponse(token, "3600", user.getUsername());
    }
}
