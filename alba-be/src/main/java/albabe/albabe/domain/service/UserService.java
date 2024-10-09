//package albabe.albabe.domain.service;
//
//import albabe.albabe.domain.entity.UserEntity;
//import albabe.albabe.domain.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//public class UserService {
//    @Autowired
//    private UserRepository userRepository;
//
//    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//    public void registerUser(String username, String password, String role, String name, String birthDate, String email, String phone, String businessNumber) {
//        UserEntity user = new UserEntity();
//        user.setUsername(username);
//        user.setPassword(passwordEncoder.encode(password));
//        user.setRole(role);
//        user.setName(name);
//        user.setBirthDate(birthDate);
//        user.setEmail(email);
//        user.setPhone(phone);
//        user.setBusinessNumber(businessNumber);
//        userRepository.save(user);
//    }
//
//    public UserEntity login(String username, String password) {
//        UserEntity user = userRepository.findByUsername(username);
//        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
//            return user; // 로그인 성공
//        }
//        return null; // 로그인 실패
//    }
//}
//
package albabe.albabe.domain.service;

import albabe.albabe.domain.entity.UserEntity;
import albabe.albabe.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void registerUser(String username, String password, String role, String name, String birthDate, String email, String phone, String businessNumber) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(encodePassword(password)); // 비밀번호 인코딩
        user.setRole(role);
        user.setName(name);
        user.setBirthDate(birthDate);
        user.setEmail(email);
        user.setPhone(phone);
        user.setBusinessNumber(businessNumber);
        userRepository.save(user);
    }

    public UserEntity login(String username, String password) {
        UserEntity user = userRepository.findByUsername(username);
        if (user != null && matchesPassword(password, user.getPassword())) {
            return user; // 로그인 성공
        }
        return null; // 로그인 실패
    }

    // 비밀번호 인코딩 메서드
    private String encodePassword(String password) {
        // Base64 인코딩을 사용하여 비밀번호를 인코딩
        return Base64.getEncoder().encodeToString(password.getBytes());
    }

    // 비밀번호 매칭 메서드
    private boolean matchesPassword(String rawPassword, String encodedPassword) {
        // Base64 디코딩 후 비교
        String decodedPassword = new String(Base64.getDecoder().decode(encodedPassword));
        return rawPassword.equals(decodedPassword);
    }
}
