package What2Do.controller;

import What2Do.domain.*;
import What2Do.service.BoardService;
import What2Do.service.CityService;
import What2Do.service.CommentService;
import What2Do.service.TourService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private final TourService tourService;
    private final CommentService commentService;
    private final EntityManager entityManager;
    private final BoardService boardService;
    private final CityService cityService;

    public MainController(TourService tourService, CommentService commentService, EntityManager entityManager, BoardService boardService, CityService cityService) {
        this.tourService = tourService;
        this.commentService = commentService;
        this.entityManager = entityManager;
        this.boardService = boardService;
        this.cityService = cityService;
    }

    @RequestMapping("/lego")
    public String main() {

        return "/main/what2do";
    }

    @RequestMapping("/")
    public String mainview(Model model) {
        List<Board> blist = boardService.findNotices();
        model.addAttribute("blist", blist);
        List<Tour> bestFive = tourService.findBest();
        model.addAttribute("bestFive",bestFive);
        return "/main/mainview";
    }


    @GetMapping("join")
    public String join(Model model) {
        UserDTO userDTO = new UserDTO();
        model.addAttribute("user", userDTO);
        return "/member/join";
    }

//    @RequestMapping(value = "/what2do", method = RequestMethod.GET)
//    public String home() {
//
//        return "main/mainview";
//    }

    @RequestMapping(value = "/main/adminmain", method = RequestMethod.GET)
    public String adminmain() {

        return "main/adminmain";
    }

    @GetMapping("gyeonggi")
    public String gyeonggi() {

        return "tour/gyeonggi";
    }

    @GetMapping("southjeolla")
    public String southjeolla() {

        return "tour/southjeolla";
    }

    @GetMapping("seoul")
    public String seoul() {

        return "tour/seoul";
    }

    @GetMapping("/chungnam")
    public String chungnam() {
        return "tour/chungnam";
    }

    @GetMapping("/gyeongnam")
    public String gyeongnam() {
        return "tour/gyeongnam";
    }

    @GetMapping("/gangwon")
    public String gangwon() {
        return "tour/gangwon";
    }

    @GetMapping("/jeonbuk")
    public String jeonbuk() {
        return "tour/jeonbuk";
    }

    @GetMapping("chungbuk")
    public String chungbuk() {
        return "tour/chungbuk";
    }

    @GetMapping("gyeongbuk")
    public String gyeongbuk() {
        return "tour/gyeongbuk";
    }

    @GetMapping("jeju")
    public String jeju() {
        return "tour/jeju";
    }
    @GetMapping("incheon")
    public String incheon() {
        return "tour/incheon";
    }
    @GetMapping("ulsan")
    public String ulsan() {
        return "tour/ulsan";
    }
    @GetMapping("sejong")
    public String sejong() {
        return "tour/sejong";
    }
    @GetMapping("gwangju")
    public String gwangju() {
        return "tour/gwangju";
    }
    @GetMapping("daejeon")
    public String daejeon() {
        return "tour/daejeon";
    }
    @GetMapping("daegu")
    public String daegu() {
        return "tour/daegu";
    }
    @GetMapping("busan")
    public String busan() {
        return "tour/busan";
    }






    @GetMapping("/category")
    public String category2(@RequestParam("city") String city, @RequestParam("areacode") String areacode, @RequestParam("sigungucode") String sigungucode, Model model) {
        List<Tour> tlist = tourService.findRegion(areacode, sigungucode);
        model.addAttribute("city", city);
        model.addAttribute("tlist", tlist);
        return "tour/city";
    }

    @GetMapping("/detail")
    public String detailV(@RequestParam("id") Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        String num = user.getId();
        Tour tour = tourService.findOne(id);
        model.addAttribute("tour", tour);
        boolean like = tourService.likeB(id, num);
        model.addAttribute("like", like);
        List<Comment> clist = commentService.commentV(id);
        if (clist != null) {
            model.addAttribute("clist", clist);
        }
        return "tour/cityDetail";
    }

    //    @ResponseBody
    @PostMapping("/categoryView")
    public String categoryV(@RequestParam("num") String contenttypeid, @RequestParam("areacode") String areacode, @RequestParam("sigungucode") String sigungucode, Model model) {
        List<Tour> tlist = tourService.findCatecory(contenttypeid, areacode, sigungucode);
        model.addAttribute("tlist", tlist);
        return "tour/city::#categoryTable";
    }

    //댓글 저장
    @PostMapping("/commentR")
    public String commentR(Comment comment, @RequestParam("tour_id") Tour tour_id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        String user_id = user.getId();
        comment.setTour(tour_id);
        comment.setUser(user_id);
        commentService.commentR(comment);
        Long tid = tour_id.getId();
        return "redirect:/detail?id=" + tid;
    }

    @ResponseBody
    @PostMapping("/deleteC")
    public void deleteC(@RequestParam("no") Long no) {
        commentService.commentD(no);
    }

    @ResponseBody
    @PostMapping("/modifyC")
    public String modifyC(@RequestParam("no") Long no) {
        Comment commentV = commentService.commentM(no);
        String comment = commentV.getContent();
        return comment;
    }

    @ResponseBody
    @PostMapping("/updateC")
    public void updateC(@RequestParam("no") Long no, @RequestParam("newComment") String content) {
        commentService.commentU(no, content);
    }

    @ResponseBody
    @PostMapping("/likeT")
    @Transactional
    public HashMap<String, Object> likeB(@RequestParam("num") Long num,
                                         HttpSession session) {
        User user = (User) session.getAttribute("user");
        String id = user.getId();
        boolean like = tourService.likeB(num, id);
        if (like == false) {
            HashMap<String, Object> likeAll = new HashMap<>();
            tourService.likeI(num, id);
            tourService.likePlus(num);
            entityManager.flush();
            entityManager.clear();
            Tour tour = tourService.view(num);
            Integer likeCnt = tour.getLike_count();
            likeAll.put("likeCnt", likeCnt);
            likeAll.put("like", like);
            return likeAll;
        } else {
            HashMap<String, Object> likeAll = new HashMap<>();
            tourService.likeD(num, id);
            tourService.likeMinus(num);
            Tour tour = tourService.view(num);
            Integer likeCnt = tour.getLike_count();
            likeAll.put("likeCnt", likeCnt);
            likeAll.put("like", like);
            return likeAll;
        }


    }
    @GetMapping("/what2do")
    public String mainList(Model model) {
        List<Board> blist = boardService.findNotices();
        model.addAttribute("blist", blist);
        List<Tour> bestFive = tourService.findBest();
        model.addAttribute("bestFive",bestFive);
        return "main/mainview";
    }
//    @GetMapping("/best")
//    public String best(Model model){
//        List<Tour> bestFive = tourService.findBest();
//        model.addAttribute("bestFive",bestFive);
//        return "main/";
//    }
}