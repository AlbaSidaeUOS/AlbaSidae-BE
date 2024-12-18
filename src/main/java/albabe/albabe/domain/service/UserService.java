package albabe.albabe.domain.service;

import albabe.albabe.domain.dto.UserDto;
import albabe.albabe.domain.entity.*;
import albabe.albabe.domain.enums.UserRole;
import albabe.albabe.domain.repository.*;
import albabe.albabe.security.JwtTokenProvider;
import albabe.albabe.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.List;
import java.util.stream.Collectors;

/*
    Description :  회원 Service 소스파일. 회원가입, 로그인, 아이디/비밀번호 찾기,
    회원 정보 수정, 사용자 삭제, 유저 이미지 업로드 함수 구현
 */

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ResumeRepository resumeRepository;
    private final JobPostRepository jobPostRepository;
    private final JobApplicationRepository jobApplicationRepository;
    @Autowired
    private TimeTableRepository timeTableRepository;

    public UserService(UserRepository userRepository, ResumeRepository resumeRepository,
                       JobPostRepository jobPostRepository, JobApplicationRepository jobApplicationRepository) {
        this.userRepository = userRepository;
        this.resumeRepository = resumeRepository;
        this.jobPostRepository = jobPostRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private S3Service s3Service;

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

        // 생년월일 유효성 검증
        if (userDto.getRole() == UserRole.PERSONAL && !ValidationUtils.isValidBirthDate(userDto.getBirthDate())) {
            throw new IllegalArgumentException("생년월일이 올바르지 않습니다. (예: YYMMDD, 유효한 날짜만 허용)");
        }

        // 사업자 번호 길이 확인 (10자리)
        if (userDto.getRole() == UserRole.COMPANY && userDto.getBusinessNumber().length() != 10) {
            throw new IllegalArgumentException("사업자 번호는 10자리여야 합니다.");
        }

        // 핸드폰 번호 길이 확인 (11자리)
        if (userDto.getPhone().length() != 11) {
            throw new IllegalArgumentException("핸드폰 번호는 11자리여야 합니다.");
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
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
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

    public List<UserDto> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();

        // UserEntity를 UserDto로 변환
        return users.stream()
                .map(user -> new UserDto(
                        user.getId(),
                        user.getEmail(),
                        null, // 비밀번호는 제외
                        user.getName(),
                        user.getBirthDate(),
                        user.getGender(),
                        user.getPhone(),
                        user.getBusinessNumber(),
                        user.getImage(),
                        user.getRole()
                ))
                .collect(Collectors.toList());
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

        // 생년월일 유효성 검증
        if (user.getRole() == UserRole.PERSONAL && !ValidationUtils.isValidBirthDate(userDto.getBirthDate())) {
            throw new IllegalArgumentException("생년월일이 올바르지 않습니다. (예: YYMMDD, 유효한 날짜만 허용)");
        }

        // 핸드폰 번호 길이 확인 (11자리)
        if (userDto.getPhone().length() != 11) {
            throw new IllegalArgumentException("핸드폰 번호는 11자리여야 합니다.");
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
    @Transactional
    public void deleteUserByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 1. 이력서 삭제
        List<ResumeEntity> resumes = resumeRepository.findAllByPersonal(user);
        if (!resumes.isEmpty()) {
            resumeRepository.deleteAll(resumes);
        }

        // 2. 구인공고와 관련된 지원자 및 구인공고 삭제
        List<JobPostEntity> jobPosts = jobPostRepository.findAllByCompany(user);
        if (!jobPosts.isEmpty()) {
            for (JobPostEntity jobPost : jobPosts) {
                // 구인공고에 지원한 지원자 삭제
                List<JobApplicationEntity> jobApplications = jobApplicationRepository.findAllByJobPost(jobPost);
                if (!jobApplications.isEmpty()) {
                    jobApplicationRepository.deleteAll(jobApplications);
                }
            }
            jobPostRepository.deleteAll(jobPosts);
        }

        // 3. 이력서 삭제
        Optional<TimeTableEntity> timetable = timeTableRepository.findByEmail(user.getEmail());
        if (!timetable.isEmpty()) {
            timeTableRepository.delete(timetable.get());
        }

        // 4. 사용자 삭제
        userRepository.delete(user);
    }


    // 아이디(이메일) 찾기 메서드
    public String findEmail(String name, String phone, UserRole role, String businessNumber) {
        Optional<UserEntity> user;
        if (role == UserRole.PERSONAL) {
            user = userRepository.findByNameAndPhoneAndRole(name, phone, role);
        } else {
            user = userRepository.findByNameAndPhoneAndRoleAndBusinessNumber(name, phone, role, businessNumber);
        }
        return user.orElseThrow(() -> new IllegalArgumentException("일치하는 사용자 정보를 찾을 수 없습니다.")).getEmail();
    }
    /*
    // 비밀번호 재설정 메서드
    public void resetPassword(String email, String name, String phone, UserRole role, String businessNumber, String newPassword) {
        // 유효성 검사
        if (newPassword.length() < 8 || newPassword.length() > 15) {
            throw new IllegalArgumentException("비밀번호는 8자에서 15자 사이여야 합니다.");
        }
        Optional<UserEntity> user;
        if (role == UserRole.PERSONAL) {
            user = userRepository.findByEmailAndNameAndPhoneAndRole(email, name, phone, role);
        } else {
            user = userRepository.findByEmailAndNameAndPhoneAndRoleAndBusinessNumber(email, name, phone, role, businessNumber);
        }

        UserEntity userEntity = user.orElseThrow(() -> new IllegalArgumentException("일치하는 사용자 정보를 찾을 수 없습니다."));
        userEntity.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userEntity);
    }*/

    public boolean verifyUser(String email, String name, String phone, UserRole role, String businessNumber) {
        Optional<UserEntity> user;
        if (role == UserRole.PERSONAL) {
            user = userRepository.findByEmailAndNameAndPhoneAndRole(email, name, phone, role);
        } else {
            user = userRepository.findByEmailAndNameAndPhoneAndRoleAndBusinessNumber(email, name, phone, role, businessNumber);
        }
        return user.isPresent();
    }

    // 비밀번호 재설정 메서드
    public void resetPassword(String email, String newPassword) {
        if (newPassword.length() < 8 || newPassword.length() > 15) {
            throw new IllegalArgumentException("비밀번호는 8자에서 15자 사이여야 합니다.");
        }

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자를 찾을 수 없습니다."));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public String uploadUserImage(String email, MultipartFile file) {
        try {
            // S3에 이미지 업로드 후 URL 반환
            String imageUrl = s3Service.uploadFile(file);

            // 사용자 엔티티 가져오기 및 이미지 URL 저장
            UserEntity user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자를 찾을 수 없습니다."));

            user.setImage(imageUrl);  // URL을 저장
            userRepository.save(user);

            return imageUrl;
        } catch (IOException e) {
            throw new IllegalArgumentException("파일 업로드 중 오류가 발생했습니다.", e);
        }
    }
}
