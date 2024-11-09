package albabe.albabe.domain.service;

import albabe.albabe.domain.dto.CompanyDto;
import albabe.albabe.domain.dto.JobPostResponse;
import albabe.albabe.domain.dto.ResumeDto;
import albabe.albabe.domain.dto.UserDto;
import albabe.albabe.domain.entity.JobPostEntity;
import albabe.albabe.domain.entity.ResumeEntity;
import albabe.albabe.domain.entity.UserEntity;
import albabe.albabe.domain.enums.UserRole;
import albabe.albabe.domain.repository.ResumeRepository;
import albabe.albabe.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;
    @Autowired
    private UserRepository userRepository;

    public ResumeDto createResume(ResumeEntity resumeEntity, String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회사 계정을 찾을 수 없습니다."));

        resumeEntity.setUser(user);

        ResumeEntity savedResume = resumeRepository.save(resumeEntity);

        UserDto userDto = new UserDto(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getName(),
                user.getBirthDate(),
                user.getGender(),
                user.getPhone(),
                user.getBusinessNumber(),
                user.getImage(),
                user.getRole()
        );

        return new ResumeDto(
                savedResume.getId(),
                savedResume.getResumeTitle(),
                savedResume.getSelfIntroduction(),
                savedResume.getEducationLevel(),
                savedResume.getPreferredWorkLocation(),
                savedResume.getPreferredJobTypes(),
                savedResume.getEmploymentTypes(),
                savedResume.getWorkPeriod(),
                savedResume.getWorkDays(),
                userDto
        );
    }

    public ResumeDto getResumeById(Long id) {
        ResumeEntity resumeEntity = resumeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("이력서를 찾을 수 없습니다."));
        return convertToDto(resumeEntity);
    }

    public ResumeDto updateResume(Long id, ResumeEntity updatedResumeDto, String email) {
        ResumeEntity existingResume = resumeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("이력서를 찾을 수 없습니다."));

        if (!existingResume.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("해당 구인 공고를 수정할 권한이 없습니다.");
        }

        existingResume.setResumeTitle(updatedResumeDto.getResumeTitle());
        existingResume.setSelfIntroduction(updatedResumeDto.getSelfIntroduction());
        existingResume.setEducationLevel(updatedResumeDto.getEducationLevel());
        existingResume.setPreferredWorkLocation(updatedResumeDto.getPreferredWorkLocation());
        existingResume.setPreferredJobTypes(updatedResumeDto.getPreferredJobTypes());
        existingResume.setEmploymentTypes(updatedResumeDto.getEmploymentTypes());
        existingResume.setWorkPeriod(updatedResumeDto.getWorkPeriod());
        existingResume.setWorkDays(updatedResumeDto.getWorkDays());

        ResumeEntity savedResume = resumeRepository.save(existingResume);

        return convertToDto(savedResume);
    }

    public void deleteResume(Long id, String email) {
        ResumeEntity resume = resumeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("구인 공고를 찾을 수 없습니다."));

        if (!resume.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("해당 구인 공고를 삭제할 권한이 없습니다.");
        }

        resumeRepository.delete(resume);
    }

    private ResumeDto convertToDto(ResumeEntity resume) {
        UserDto userDto = new UserDto(
                resume.getUser().getId(),
                resume.getUser().getEmail(),
                resume.getUser().getPassword(),
                resume.getUser().getName(),
                resume.getUser().getBirthDate(),
                resume.getUser().getGender(),
                resume.getUser().getPhone(),
                resume.getUser().getBusinessNumber(),
                resume.getUser().getImage(),
                resume.getUser().getRole()
        );

        return new ResumeDto(
                resume.getId(),
                resume.getResumeTitle(),
                resume.getSelfIntroduction(),
                resume.getEducationLevel(),
                resume.getPreferredWorkLocation(),
                resume.getPreferredJobTypes(),
                resume.getEmploymentTypes(),
                resume.getWorkPeriod(),
                resume.getWorkDays(),
                userDto
        );
    }
}
