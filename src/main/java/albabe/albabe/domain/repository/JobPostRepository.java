package albabe.albabe.domain.repository;

import albabe.albabe.domain.entity.JobPostEntity;
import albabe.albabe.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JobPostRepository extends JpaRepository<JobPostEntity, Long> {
    List<JobPostEntity> findByCompany(UserEntity company);

    @Query("SELECT j FROM JobPostEntity j WHERE " +
            "(:workDays IS NULL OR j.workDays IN :workDays) AND " +
            "(:workCategory IS NULL OR j.workCategory IN :workCategory) AND " +
            "(:workType IS NULL OR j.workType IN :workType) AND " +
            "(:workTime IS NULL OR j.workTime = :workTime) AND " +
            "(:pay IS NULL OR j.pay = :pay) AND " +
            "(:gender IS NULL OR j.gender = :gender) AND " +
            "(:age IS NULL OR j.age = :age)")
    List<JobPostEntity> findJobPostsByFilter(
            List<String> workDays,
            List<String> workCategory,
            List<String> workType,
            String workTime,
            String pay,
            String gender,
            String age);

}
