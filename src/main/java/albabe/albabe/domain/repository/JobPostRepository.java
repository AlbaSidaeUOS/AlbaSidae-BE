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
                    "JOIN job_post_entity_work_category as category ON post.id = category.job_post_entity_id " +
                    "WHERE (:workLocations IS NULL OR post.place IN (:workLocations)) AND " +
                    "(:workDays IS NULL OR days.work_days IN (:workDays)) AND " +
                    "(:workCategories IS NULL OR category.work_category IN (:workCategories)) AND " +
                    "(:workTimes IS NULL OR post.work_time IN (:workTimes)) AND " +
                    "(:workTerms IS NULL OR post.work_term IN (:workTerms))", nativeQuery = true)
    List<JobPostEntity> findJobPostsByFilter(
            @Param("workLocations")List<String> workLocations,
            @Param("workDays")List<String> workDays,
            @Param("workCategories")List<String> workCategories,
            @Param("workTimes")List<String> workTimes,
            @Param("workTerms")List<String> workTerms
    );
}