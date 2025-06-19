package What2Do.controller;

import What2Do.domain.*;
import What2Do.repository.TourRepository;
import What2Do.service.BoardService;
import What2Do.service.CommentService;
import What2Do.service.TourService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private final TourService tourService;
    private final TourRepository tourRepository;
    private final CommentService commentService;
    private final EntityManager entityManager;
    private final BoardService boardService;

    public MainController(TourService tourService, CommentService commentService, EntityManager entityManager, BoardService boardService, TourRepository tourRepository) {
        this.tourService = tourService;
        this.commentService = commentService;
        this.entityManager = entityManager;
        this.boardService = boardService;
        this.tourRepository = tourRepository;
    }


    @RequestMapping("/tourAdmin")
    public String tourAdmin(Model model) {
        List<Tour> tlist = tourService.findAll();
        //list에 findAll 관광지 정보 대입
        System.out.println("관리자모드 - 놀러가자");
        model.addAttribute("tlist", tlist);
        return "admin/tourList";
    }

    @RequestMapping("/viewTour/{id}")
    public String viewTour(@PathVariable Long id, Model model) {
        Tour tour = tourService.findOne(id);
        model.addAttribute("tour", tour);
        return "admin/viewTour";
    }

    @GetMapping("/detail")
    public String detailV(@RequestParam("id") Long id,
                          @RequestParam("city") String city,
                          Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        String num = user.getId();

        Tour tour = tourService.findOne(id);
        model.addAttribute("tour", tour);

        boolean like = tourService.likeB(id, num);
        model.addAttribute("like", like);
        model.addAttribute("city", city);
        List<Comment> clist = commentService.commentV(id);
        System.out.println("city: "+city);
        if (clist != null) {
            model.addAttribute("clist", clist);
        }
        return "tour/cityDetail";
    }

    //댓글 저장
    @PostMapping("/commentR")
    public String commentR(Comment comment, @RequestParam("tour_id") Tour tour_id,
                           @RequestParam("city") String city, RedirectAttributes re, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        String user_id = user.getId();
        comment.setTour(tour_id);
        comment.setUser(user_id);
        commentService.commentR(comment);
        Long tid = tour_id.getId();
        re.addAttribute("city", city);
        return "redirect:/detail?id=" + tid;
    }

    @PostMapping("/updateTour/{id}")
    public String updateTour(@PathVariable Long id,
                             @RequestParam("title") String title,
                             @RequestParam("addr1") String addr1,
                             @RequestParam("overview") String overview,
                             @RequestParam("firstimage") String firstimage,
                             @RequestParam("firstimage2") String firstimage2,
                             @RequestParam("city") String city,
                             RedirectAttributes re){
        Tour tour = tourService.findOne(id);
        tour.setTitle(title);
        tour.setAddr1(addr1);
        tour.setOverview(overview);
        tour.setFirstimage(firstimage);
        tour.setFirstimage2(firstimage2);
        tourRepository.save(tour);
        re.addAttribute("city", city);
        re.addAttribute("id", id);
        System.out.println(id+city);
        return "redirect:/detail";
    }

    @RequestMapping("addTour")
    public String addTour(@RequestParam("city")String city,
                          @RequestParam("sigungucode")String sigungucode,
                          @RequestParam("areacode")String areacode,
                          RedirectAttributes re, Model model){
        re.addAttribute("city", city);
        re.addAttribute("sigungucode", sigungucode);
        re.addAttribute("areacode", areacode);
        model.addAttribute("city", city);
        model.addAttribute("areacode", areacode);
        model.addAttribute("sigungucode", sigungucode);
        System.out.println(city+" 지역코드: "+areacode+" 시군구코드: "+sigungucode);
        return "admin/addTour";
    }

    @PostMapping("saveTour")
    public String saveTour(@RequestParam("contentid")String contentid,
                           @RequestParam("sigungucode")String sigungucode,
                           @RequestParam("addr1")String addr1,
                           @RequestParam("addr1")String addr2,
                           @RequestParam("overview")String overview,
                           @RequestParam("firstimage")String firstimage,
                           @RequestParam("firstimage2")String firstimage2,
                           @RequestParam("cat1")String cat1,
                           @RequestParam("cat2")String cat2,
                           @RequestParam("cat3")String cat3,
                           @RequestParam("title")String title,
                           @RequestParam("mapx")Double mapx,
                           @RequestParam("mapy")Double mapy,
                           @RequestParam("areacode")String areacode,
                           @RequestParam("contenttypeid")String contenttypeid,
                           @RequestParam("city")String city,
                           RedirectAttributes re){
        Tour tour = new Tour();
        tour.setContentid(contentid);
        tour.setSigungucode(sigungucode);
        tour.setAddr1(addr1);
        tour.setAddr2(addr2);
        tour.setOverview(overview);
        tour.setFirstimage(firstimage);
        tour.setFirstimage2(firstimage2);
        tour.setCat1(cat1);
        tour.setCat2(cat2);
        tour.setCat3(cat3);
        tour.setTitle(title);
        tour.setMapx(mapx);
        tour.setMapy(mapy);
        tour.setContenttypeid(contenttypeid);
        tour.setAreacode(areacode);
        tourRepository.save(tour);

        System.out.println(city+areacode+sigungucode);
        re.addAttribute("city", city);
        re.addAttribute("areacode", areacode);
        re.addAttribute("sigungucode", sigungucode);
        return "redirect:/category";
    }

    @GetMapping("deleteTour/{id}")
    public String deleteTour(@PathVariable("id")Long id,
                             @RequestParam("city") String city,
                             @RequestParam("areacode") String areacode,
                             @RequestParam("sigungucode") String sigungucode,
                             RedirectAttributes re) {
        System.out.println("id: "+id+" city: "+city+" areacode: "+areacode+" sigungucode: "+sigungucode);
        tourService.delete(id);
        re.addAttribute("city", city);
        re.addAttribute("areacode", areacode);
        re.addAttribute("sigungucode", sigungucode);
        System.out.println(city+areacode+"/"+sigungucode);
        return "redirect:/category";
    }

    @GetMapping("/category")
    public String category2(@RequestParam("city") String city, @RequestParam("areacode") String areacode, @RequestParam("sigungucode") String sigungucode, Model model) {
        List<Tour> tlist = tourService.findRegion(areacode, sigungucode);
        model.addAttribute("city", city);
        model.addAttribute("areacode", areacode);
        model.addAttribute("sigungucode", sigungucode);
        model.addAttribute("tlist", tlist);

        System.out.println("/category 컨트롤러 실행됨, areacode: "+areacode+" sugungucode: "+sigungucode);

        return "tour/city";
    }

    @GetMapping("/searchTour")
    public String searchTour(@RequestParam("contentid") String contentid, Model model) {
        System.out.println("관광지 아이디 검색");
        return "admin/tourList";
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
        return "main/mainview";
    }

    @RequestMapping("/testDot")
    public String testDot() {
        return "/main/testDot";
    }


    @GetMapping("join")
    public String join(Model model) {
        UserDTO userDTO = new UserDTO();
        model.addAttribute("user", userDTO);
        return "/member/join";
    }

    @GetMapping("/what2do")
    public String mainList(Model model) {
        List<Board> blist = boardService.findNotices();
        model.addAttribute("blist", blist);
        List<Tour> bestFive = tourService.findBest();
        model.addAttribute("bestFive", bestFive);
        return "main/mainview";
    }



    //    @ResponseBody
    @PostMapping("/categoryView")
    public String categoryV(@RequestParam("num") String contenttypeid, @RequestParam("areacode") String areacode, @RequestParam("sigungucode") String sigungucode, Model model) {
        List<Tour> tlist = tourService.findCatecory(contenttypeid, areacode, sigungucode);
        model.addAttribute("tlist", tlist);
        return "tour/city::#categoryTable";
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

}