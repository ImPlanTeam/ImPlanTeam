package What2Do.service;

import What2Do.domain.Activity;
import What2Do.repository.ActivityRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public List<Activity> recommend(String weather, String mood, String companions, String tags) {
        List<Activity> rlist = activityRepository.findByWeatherAndMoodAndCompanionsAndTags(weather,mood,companions,tags);
        return rlist;
    }
    public List<Activity> allList() {
        List<Activity> alist = activityRepository.findAll();
        return alist;
    }

    public List<Activity> oneView(String name){
        List<Activity> list = activityRepository.findByName(name);
        return list;
    }
    public void deleteOne(Integer id){
        activityRepository.deleteById(id);
    }
    public Activity updateOne(Integer id){
        Activity one=activityRepository.findById(id).orElseThrow();
        return one;
    }
    public void newSave(Activity activity){
        activityRepository.save(activity);
    }

    public void register(Activity activity){
        activityRepository.save(activity);
    }

    public boolean count(String name){
        boolean count = activityRepository.existsByName(name);
        return count;
    }
    public List<Activity> searchToday(String name){
        return activityRepository.findByNameContaining(name);
    }
}