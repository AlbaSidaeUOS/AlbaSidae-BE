package albabe.albabe.domain.service;

import albabe.albabe.domain.dto.JobPostResponse;
import albabe.albabe.domain.dto.PersonalDto;
import albabe.albabe.domain.dto.ResumeDto;
import albabe.albabe.domain.entity.JobPostEntity;
import albabe.albabe.domain.entity.ResumeEntity;
import albabe.albabe.domain.entity.UserEntity;
import albabe.albabe.domain.enums.UserRole;
import albabe.albabe.domain.repository.JobApplicationRepository;
import albabe.albabe.domain.repository.ResumeRepository;
import albabe.albabe.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/*
    Description : 이력서 Service 소스파일. 이력서 등록/수정/삭제 관련 함수들 구현
 */

@Service
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    public ResumeDto createResume(ResumeEntity resumeEntity, String email) {
        UserEntity personal = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을 수 없습니다."));

        resumeEntity.setPersonal(personal);

        ResumeEntity savedResume = resumeRepository.save(resumeEntity);

        PersonalDto personalDto = new PersonalDto(
                personal.getId(),
                personal.getEmail(),
                personal.getName(),
                personal.getBirthDate(),
                personal.getGender(),
                personal.getPhone(),
                personal.getImage(),
                personal.getRole()
        );

        return new ResumeDto(
                savedResume.getId(),
                savedResume.getResumeTitle(),
                savedResume.getSelfIntroduction(),
                savedResume.getEducationLevel(),
                savedResume.getCareer(),
                savedResume.getPreferredWorkLocation(),
                savedResume.getPreferredJobTypes(),
                savedResume.getEmploymentTypes(),
                savedResume.getWorkPeriod(),
                savedResume.getWorkDays(),
                personalDto
        );
    }

    // 공고 조회 (전체)
    public List<ResumeDto> getAllResumes() {
        List<ResumeEntity> resumes = resumeRepository.findAll();
        return resumes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 공고 조회 (전체)
    public List<ResumeDto> getResumes(String email) {
        List<ResumeEntity> resumes = resumeRepository.findResumesByEmail(email);
        // 이메일이 제공되었지만 해당 이메일로 공고가 조회되지 않는 경우
        if (email != null && !email.isEmpty() && resumes.isEmpty()) {
            throw new IllegalArgumentException("입력한 이메일로 조회된 이력서가 없습니다.");
        }
        return resumes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    public ResumeDto getResumeById(Long id) {
        ResumeEntity resumeEntity = resumeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("이력서를 찾을 수 없습니다."));
        return convertToDto(resumeEntity);
    }

    public ResumeDto updateResume(Long id, ResumeEntity updatedResumeDto, String email) {
        ResumeEntity existingResume = resumeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("이력서를 찾을 수 없습니다."));

        if (!existingResume.getPersonal().getEmail().equals(email)) {
            throw new IllegalArgumentException("해당 이력서를 수정할 권한이 없습니다.");
        }

        existingResume.setResumeTitle(updatedResumeDto.getResumeTitle());
        existingResume.setSelfIntroduction(updatedResumeDto.getSelfIntroduction());
        existingResume.setEducationLevel(updatedResumeDto.getEducationLevel());
        existingResume.setCareer(updatedResumeDto.getCareer());
        existingResume.setPreferredWorkLocation(updatedResumeDto.getPreferredWorkLocation());
        existingResume.setPreferredJobTypes(updatedResumeDto.getPreferredJobTypes());
        existingResume.setEmploymentTypes(updatedResumeDto.getEmploymentTypes());
        existingResume.setWorkPeriod(updatedResumeDto.getWorkPeriod());
        existingResume.setWorkDays(updatedResumeDto.getWorkDays());

        ResumeEntity savedResume = resumeRepository.save(existingResume);

        return convertToDto(savedResume);
    }

    @Transactional
    public void deleteResume(Long id, String email) {
        ResumeEntity resume = resumeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("이력서를 찾을 수 없습니다."));

        if (!resume.getPersonal().getEmail().equals(email) && !isAdmin(email)) {
            throw new IllegalArgumentException("해당 이력서를 삭제할 권한이 없습니다.");
        }

        // 이력서를 참조하는 지원 내역 삭제
        jobApplicationRepository.deleteByResume(resume);

        resumeRepository.delete(resume);
    }

    // 관리자인지 확인하는 메서드
    private boolean isAdmin(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return user.getRole() == UserRole.ADMIN; // UserRole을 이용해 ADMIN 확인
    }

    private ResumeDto convertToDto(ResumeEntity resume) {
        PersonalDto personalDto = new PersonalDto(
                resume.getPersonal().getId(),
                resume.getPersonal().getEmail(),
                resume.getPersonal().getName(),
                resume.getPersonal().getBirthDate(),
                resume.getPersonal().getGender(),
                resume.getPersonal().getPhone(),
                resume.getPersonal().getImage(),
                resume.getPersonal().getRole()
        );

        return new ResumeDto(
                resume.getId(),
                resume.getResumeTitle(),
                resume.getSelfIntroduction(),
                resume.getEducationLevel(),
                resume.getCareer(),
                resume.getPreferredWorkLocation(),
                resume.getPreferredJobTypes(),
                resume.getEmploymentTypes(),
                resume.getWorkPeriod(),
                resume.getWorkDays(),
                personalDto
        );
    }
}
