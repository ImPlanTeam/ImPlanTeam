package What2Do.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TodayController {

    @GetMapping("/todo")
    public String todo(){

        return "today/index";
    }

    @GetMapping("/form")
    public String form(){

        return "today/form";
    }
}
