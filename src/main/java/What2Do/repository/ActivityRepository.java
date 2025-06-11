package What2Do.repository;

import What2Do.domain.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity,Integer> {
    List<Activity> findByWeatherAndMoodAndCompanionsAndTags(String weather, String mood, String companions,String tags);
}
