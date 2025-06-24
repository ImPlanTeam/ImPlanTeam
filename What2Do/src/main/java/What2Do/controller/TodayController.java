package What2Do.controller;

import What2Do.domain.Activity;
import What2Do.domain.City;
import What2Do.domain.Tour;
import What2Do.service.ActivityService;
import What2Do.service.CityService;
import What2Do.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TodayController {
    @Autowired
    private final ActivityService activityService;
    private final CityService cityService;

    public TodayController(ActivityService activityService, CityService cityService) {
        this.activityService = activityService;
        this.cityService = cityService;
    }

    @GetMapping("/todo")
    public String todo(){

        return "today/form";
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
    @GetMapping("/admin/today")
    public String todayV(Model model){
        List<Activity>alist = activityService.allList();
        model.addAttribute("list",alist);
        return "admin/todayAdmin";
    }
    @GetMapping("/todayAdmin")
    public String todayA(Model model,@RequestParam("name") String name){
        List<Activity> list = activityService.oneView(name);
        if (list == null || list.isEmpty()) {
            return "admin/todayAdminD";  // 에러 메시지를 표시할 뷰 (원하는대로 수정)
        }
        String name1=list.get(0).getName();
        model.addAttribute("list",list);
        model.addAttribute("name",name1);
        return "admin/todayAdminD";
    }
    @ResponseBody
    @PostMapping("/deleteToday")
    public Map<String, String> todayD(@RequestParam("id")Integer id, @RequestParam("name")String name){
        activityService.deleteOne(id);
        boolean count=activityService.count(name);
        System.out.println(count);
        Map<String, String> response = new HashMap<>();
        if (!count) {
            response.put("redirect", "/admin/today"); // 목록보기
        } else {
            String eName = URLEncoder.encode(name, StandardCharsets.UTF_8);
            response.put("redirect", "/todayAdmin?name=" + eName); // 상세보기
        }
        return response;
    }
    @ResponseBody
    @GetMapping("/updateToday")
    public Activity todayU(@RequestParam("id")Integer id){
        System.out.println(id);
        Activity aOne= activityService.updateOne(id);
        return aOne;
    }

    @PostMapping("/todayUpdateC")
    public String todayUpdateC(Activity activity){
        activityService.newSave(activity);
        System.out.println(activity.getId());
        String name=activity.getName();
        String eName = URLEncoder.encode(name,  StandardCharsets.UTF_8);
        return "redirect:/todayAdmin?name="+eName;
    }

    @GetMapping("/todayWrite")
    public String todayWrite(){
        return "admin/todayWrite";
    }

    @PostMapping("/what2doR")
    public String todayR(Activity activity){
        activityService.register(activity);
        return "redirect:/admin/today";
    }
    @GetMapping("/searchWhat2do")
    public String todaySearch(@RequestParam("search")String name,Model model){
        List<Activity> list= activityService.searchToday(name);
        model.addAttribute("list",list);
        return "admin/todayAdmin";
    }





}