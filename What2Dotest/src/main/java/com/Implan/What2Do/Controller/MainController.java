package com.Implan.What2Do.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String home() {

        return "member/index";
    }

}
