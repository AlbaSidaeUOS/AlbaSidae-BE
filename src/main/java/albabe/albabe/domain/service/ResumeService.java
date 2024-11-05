package albabe.albabe.domain.service;

import albabe.albabe.domain.dto.ResumeDto;
import albabe.albabe.domain.entity.ResumeEntity;
import albabe.albabe.domain.repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    public ResumeEntity createResume(ResumeDto resumeDto) {
        validateResumeData(resumeDto);
        ResumeEntity resumeEntity = new ResumeEntity(
                resumeDto.getApplicantName(),
                resumeDto.getContactInfo(),
                resumeDto.getEmail(),
                resumeDto.getAddress(),
                resumeDto.getResumeTitle(),
                resumeDto.getSelfIntroduction()
        );
        return resumeRepository.save(resumeEntity);
    }

    public ResumeEntity getResumeById(Long id) {
        return resumeRepository.findById(id).orElseThrow(() -> new RuntimeException("Resume not found"));
    }

    public ResumeEntity updateResume(Long id, ResumeDto resumeDto) {
        validateResumeData(resumeDto);
        ResumeEntity resumeEntity = getResumeById(id);
        resumeEntity.setApplicantName(resumeDto.getApplicantName());
        resumeEntity.setContactInfo(resumeDto.getContactInfo());
        resumeEntity.setEmail(resumeDto.getEmail());
        resumeEntity.setAddress(resumeDto.getAddress());
        resumeEntity.setResumeTitle(resumeDto.getResumeTitle());
        resumeEntity.setSelfIntroduction(resumeDto.getSelfIntroduction());
        return resumeRepository.save(resumeEntity);
    }

    public void deleteResume(Long id) {
        resumeRepository.deleteById(id);
    }

    private void validateResumeData(ResumeDto resumeDto) {
        if (!Pattern.matches("^\\d{3}-\\d{4}-\\d{4}$", resumeDto.getContactInfo())) {
            throw new IllegalArgumentException("연락처 형식이 올바르지 않습니다.");
        }
        if (!Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", resumeDto.getEmail())) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }
    }
}