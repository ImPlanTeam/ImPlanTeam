package What2Do.repository;

import What2Do.domain.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {

    @Query("SELECT a FROM Activity a WHERE " +
            "(:weather IS NULL OR a.weather LIKE %:weather%) AND " +
            "(:mood IS NULL OR a.mood LIKE %:mood%) AND " +
            "(:companions IS NULL OR a.companions LIKE %:companions%) AND " +
            "(:tags IS NULL OR a.tags LIKE %:tags%) order by rand() limit 1")
    List<Activity> findByWeatherAndMoodAndCompanionsAndTags(
            @Param("weather") String weather,
            @Param("mood") String mood,
            @Param("companions") String companions,
            @Param("tags") String tags
    );
}