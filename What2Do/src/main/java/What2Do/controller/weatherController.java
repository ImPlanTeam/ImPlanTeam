package What2Do.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class weatherController {

    @GetMapping("/weather")
    public String weather(){

        return "weather/main";
    }


}
