package albabe.albabe.domain.repository;

import albabe.albabe.domain.entity.JobPostEntity;
import albabe.albabe.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobPostRepository extends JpaRepository<JobPostEntity, Long> {
    List<JobPostEntity> findByCompany(UserEntity company);

    @Query(value = "SELECT DISTINCT post.* " +
            "FROM job_posts as post " +
            "JOIN job_post_entity_work_days as days ON post.id = days.job_post_entity_id " +
            "JOIN job_post_entity_work_category as categories ON post.id = categories.job_post_entity_id " +
            "JOIN job_post_entity_work_time_category as timeCategories ON post.id = timeCategories.job_post_entity_id " +
            "WHERE EXISTS (:workLocations)  OR post.place IN :workLocations AND " +
            ":workDays IS NULL OR days.work_days IN :workDays AND " +
            ":workCategories IS NULL OR categories.work_category IN :workCategories AND " +
            ":workTimeCategories IS NULL OR timeCategories.work_time_category IN :workTimeCategories AND " +
            ":workTerms IS NULL OR post.work_term IN :workTerms", nativeQuery = true)
    List<JobPostEntity> findJobPostsByFilter(
            @Param("workLocations") List<String> workLocations,
            @Param("workDays") List<String> workDays,
            @Param("workCategories") List<String> workCategories,
            @Param("workTimeCategories") List<String> workTimeCategories,
            @Param("workTerms") List<String> workTerms
    );
}