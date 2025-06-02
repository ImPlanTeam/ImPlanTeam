package What2Do.controller;

import What2Do.domain.Tour;
import What2Do.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private final TourService tourService;

    public MainController(TourService tourService) {
        this.tourService = tourService;
    }

    @RequestMapping("/")
    public String main(){
        return "/main/what2do";
    }


    @GetMapping("join")
    public String join(){
        return "/member/join";
    }

    @RequestMapping(value = "/what2do", method = RequestMethod.GET)
    public String home() {

        return "main/what2do";
    }

    @RequestMapping(value = "/main/adminmain", method = RequestMethod.GET)
    public String adminmain() {

        return "main/adminmain";
    }

    @GetMapping("gyeonggi")
    public String gyeonggi(){

        return "tour/gyeonggi";
    }

    @GetMapping("southjeolla")
    public String southjeolla(){

        return "tour/southjeolla";
    }

    @GetMapping("seoul")
    public String seoul(){

        return "tour/seoul";
    }

    @GetMapping("/chungnam")
    public String chungnam(){
        return "tour/chungnam";
    }

    @GetMapping("/gyeongnam")
    public String gyeongnam(){
        return "tour/gyeongnam";
    }

    @GetMapping("/gangwon")
    public String gangwon(){
        return "tour/gangwon";
    }

    @GetMapping("/jeonbuk")
    public String jeonbuk(){
        return "tour/jeonbuk";
    }

    @GetMapping("chungbuk")
    public String chungbuk(){
        return "tour/chungbuk";
    }

    @GetMapping("gyeongbuk")
    public String gyeongbuk(){
        return "tour/gyeongbuk";
    }

    @GetMapping("jeju")
    public String jeju(){
        return "tour/jeju";
    }

    @GetMapping("/category")
    public String category2(@RequestParam("city")String city,@RequestParam("areacode")String areacode,@RequestParam("sigungucode")String sigungucode, Model model){
        List<Tour> tlist=tourService.findRegion(areacode,sigungucode);
        model.addAttribute("city", city);
        model.addAttribute("tlist",tlist);
        return "tour/city";
    }
    @GetMapping("/detail")
    public String detailV(@RequestParam("id") Long id, Model model){
        Tour tour = tourService.findOne(id);
        model.addAttribute("tour",tour);
        return "tour/cityDetail";
    }
//    @ResponseBody





}
