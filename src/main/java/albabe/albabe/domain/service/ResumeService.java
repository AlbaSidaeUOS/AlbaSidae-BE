package albabe.albabe.domain.service;

import albabe.albabe.domain.dto.PersonalDto;
import albabe.albabe.domain.dto.ResumeDto;
import albabe.albabe.domain.entity.ResumeEntity;
import albabe.albabe.domain.entity.UserEntity;
import albabe.albabe.domain.repository.ResumeRepository;
import albabe.albabe.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;
    @Autowired
    private UserRepository userRepository;

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
                savedResume.getPreferredWorkLocation(),
                savedResume.getPreferredJobTypes(),
                savedResume.getEmploymentTypes(),
                savedResume.getWorkPeriod(),
                savedResume.getWorkDays(),
                personalDto
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

        if (!existingResume.getPersonal().getEmail().equals(email)) {
            throw new IllegalArgumentException("해당 이력서를 수정할 권한이 없습니다.");
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
                .orElseThrow(() -> new IllegalArgumentException("이력서를 찾을 수 없습니다."));

        if (!resume.getPersonal().getEmail().equals(email)) {
            throw new IllegalArgumentException("해당 이력서를 삭제할 권한이 없습니다.");
        }

        resumeRepository.delete(resume);
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
                resume.getPreferredWorkLocation(),
                resume.getPreferredJobTypes(),
                resume.getEmploymentTypes(),
                resume.getWorkPeriod(),
                resume.getWorkDays(),
                personalDto
        );
    }
}
