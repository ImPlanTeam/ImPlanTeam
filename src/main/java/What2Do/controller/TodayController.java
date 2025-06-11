package What2Do.controller;

import What2Do.domain.Activity;
import What2Do.domain.Tour;
import What2Do.service.ActivityService;
import What2Do.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TodayController {
    @Autowired
    private final ActivityService activityService;

    public TodayController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping("/todo")
    public String todo(){

        return "today/index";
    }

    @GetMapping("/form")
    public String form(){
        return "today/form";
    }

    @GetMapping("/recommand")
    public List<Activity> recommand(Model model,
                            @RequestParam("weather")String weather,
                            @RequestParam("mood")String mood,
                            @RequestParam("companions")String companions,
                            @RequestParam("tags")String tags){
        List<Activity> rlist=activityService.recommend(weather,mood,companions,tags);
        System.out.println(rlist);
        return rlist;
    }




}
