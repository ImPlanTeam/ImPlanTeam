package What2Do.service;

import What2Do.domain.Activity;
import What2Do.repository.ActivityRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public List<Activity> recommend(String weather, String mood, String companions,String tags) {
        List<Activity> rlist = activityRepository.findByWeatherAndMoodAndCompanionsAndTags(weather,mood,companions,tags);
        return rlist;
    }
}
