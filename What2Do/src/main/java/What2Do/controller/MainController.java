package What2Do.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {

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






}
