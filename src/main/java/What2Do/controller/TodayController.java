package What2Do.controller;

import What2Do.domain.Tour;
import What2Do.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class TodayController {
    @Autowired
    private final TourService tourService;

    public TodayController(TourService tourService) {
        this.tourService = tourService;
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
