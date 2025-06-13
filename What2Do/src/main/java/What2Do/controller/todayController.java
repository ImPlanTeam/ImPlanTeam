package What2Do.controller;

import What2Do.domain.Activity;
import What2Do.domain.City;
import What2Do.repository.UserRepository;
import What2Do.service.ActivityService;
import What2Do.service.BoardService;
import What2Do.service.CityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
public class todayController {
    private final ActivityService activityService;
    private final CityService cityService;

    public todayController(ActivityService activityService, CityService cityService) {
        this.activityService=activityService;
        this.cityService=cityService;
    }
    @GetMapping("/recommend")
    public String recommend(Model model,
                            @RequestParam("weather")String weather,
                            @RequestParam("mood")String mood,
                            @RequestParam("companions")String companions,
                            @RequestParam("tags")String tags,
                            @RequestParam("area")String area,
                            @RequestParam("city")String city){
        List<Activity> rlist=activityService.recommend(weather,mood,companions,tags);
        model.addAttribute("rlist",rlist);
        model.addAttribute("area",area);
        model.addAttribute("city",city);
        return "today/view";
    }
    @GetMapping("/cityFind")
    public String areaFind(@RequestParam("area") String area,@RequestParam("city")String city){
        List<City> clist=cityService.findArea(area,city);
        String encodeArea = URLEncoder.encode(clist.get(0).getCity(), StandardCharsets.UTF_8);
        return "redirect:/category?city="+ encodeArea+"&areacode="+clist.get(0).getAreacode()+"&sigungucode="+clist.get(0).getSigungucode();
    }

    @ResponseBody
    @GetMapping("/cityListV")
    public List<City> cityList(@RequestParam("area3")String area){
        List<City> clist=cityService.cityList(area);
        return clist;
    }

    @GetMapping("/todo")
    public String todo(){

        return "today/index";
    }

    @GetMapping("/form")
    public String form(){

        return "today/form";
    }
}