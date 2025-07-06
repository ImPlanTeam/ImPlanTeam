package What2Do.controller;

import What2Do.domain.*;
import What2Do.repository.AnswerRepository;
import What2Do.repository.TourRepository;
import What2Do.service.AskService;
import What2Do.service.BoardService;
import What2Do.service.CommentService;
import What2Do.service.TourService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private final TourService tourService;
    private final TourRepository tourRepository;
    private final CommentService commentService;
    private final EntityManager entityManager;
    private final BoardService boardService;
    private final AskService askService;

    public MainController(TourService tourService, CommentService commentService, EntityManager entityManager, BoardService boardService, TourRepository tourRepository, AskService askService) {
        this.tourService = tourService;
        this.commentService = commentService;
        this.entityManager = entityManager;
        this.boardService = boardService;
        this.tourRepository = tourRepository;
        this.askService = askService;
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

        Integer count = commentService.counting(id);
        System.out.println(count);
        model.addAttribute("count", count);

        model.addAttribute("city", city);
        List<Comment> clist = commentService.commentV(id);
        System.out.println("city: " + city);
        if (clist != null) {
            model.addAttribute("clist", clist);
        }
        // Flask 추천 로직 추가
        List<Map<String, Object>> recommendedList = tourService.getRecommendedToursFromFlask(tour);
        model.addAttribute("recommendedList", recommendedList);

        return "tour/cityDetail";
    }

    //댓글 저장
    @PostMapping("/commentR")
    public String commentR(Comment comment, @RequestParam("tour_id") Tour tour_id,
                           @RequestParam("city") String city, RedirectAttributes re, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user.getName().equals("관리자")) {
            String user_id = user.getName();
            comment.setUser(user_id);
        } else {
            String user_id = user.getId();
            comment.setUser(user_id);
        }
        comment.setTour(tour_id);
        commentService.commentR(comment);
        Long tid = tour_id.getId();
        re.addAttribute("city", city);
        return "redirect:/detail?id=" + tid;
    }

    @ResponseBody
    @PostMapping("/deleteC")
    public void deleteC(@RequestParam("no") Long no) {
        commentService.commentD(no);
    }

    @ResponseBody
    @PostMapping("/updateC")
    public void updateC(@RequestParam("no") Long no, @RequestParam("newComment") String content) {
        commentService.commentU(no, content);
    }

    @ResponseBody
    @PostMapping("/updateA")
    public void updateA(@RequestParam("no") Integer no, @RequestParam("newComment") String content) {
        askService.answerU(no, content);
    }

    @ResponseBody
    @PostMapping("/modifyA")
    public String updateA(@RequestParam("no") Integer no) {
        System.out.println("답변수정 / " + no);
        Answer answerV = askService.answerM(no);
        String answer = answerV.getContent();
        return answer;
    }

    @ResponseBody
    @PostMapping("/modifyC")
    public String modifyC(@RequestParam("no") Long no) {
        System.out.println("댓글수정 / " + no);
        Comment commentV = commentService.commentM(no);
        String comment = commentV.getContent();
        return comment;
    }


    @PostMapping("/updateTour/{id}")
    public String updateTour(@PathVariable Long id,
                             @RequestParam("title") String title,
                             @RequestParam("addr1") String addr1,
                             @RequestParam("overview") String overview,
                             @RequestParam("firstimage") String firstimage,
                             @RequestParam("firstimage2") String firstimage2,
                             @RequestParam("city") String city,
                             RedirectAttributes re) {
        Tour tour = tourService.findOne(id);
        tour.setTitle(title);
        tour.setAddr1(addr1);
        tour.setOverview(overview);
        tour.setFirstimage(firstimage);
        tour.setFirstimage2(firstimage2);
        tourRepository.save(tour);
        re.addAttribute("city", city);
        re.addAttribute("id", id);
        System.out.println(id + city);
        return "redirect:/detail";
    }

    @RequestMapping("addTour")
    public String addTour(@RequestParam("city") String city,
                          @RequestParam("sigungucode") String sigungucode,
                          @RequestParam("areacode") String areacode,
                          RedirectAttributes re, Model model) {
        Tour tour = new Tour();
        model.addAttribute("tour", tour);
        re.addAttribute("city", city);
        re.addAttribute("sigungucode", sigungucode);
        re.addAttribute("areacode", areacode);
        model.addAttribute("city", city);
        model.addAttribute("areacode", areacode);
        model.addAttribute("sigungucode", sigungucode);
        System.out.println(city + " 지역코드: " + areacode + " 시군구코드: " + sigungucode);
        return "admin/addTour";
    }

    @PostMapping("saveTour")
    public String saveTour(@Valid TourDTO tourDTO,
                           @RequestParam("contentid") String contentid,
                           @RequestParam("sigungucode") String sigungucode,
                           @RequestParam("addr1") String addr1,
                           @RequestParam("addr1") String addr2,
                           @RequestParam("overview") String overview,
                           @RequestParam("firstimage") String firstimage,
                           @RequestParam("firstimage2") String firstimage2,
                           @RequestParam("cat1") String cat1,
                           @RequestParam("cat2") String cat2,
                           @RequestParam("cat3") String cat3,
                           @RequestParam("title") String title,
                           @RequestParam("mapx") Double mapx,
                           @RequestParam("mapy") Double mapy,
                           @RequestParam("areacode") String areacode,
                           @RequestParam("contenttypeid") String contenttypeid,
                           @RequestParam("city") String city,
                           RedirectAttributes re, Model model) {
        model.addAttribute("tour", tourDTO);
        model.addAttribute("city", city);
        model.addAttribute("areacode", areacode);
        model.addAttribute("sigungucode", sigungucode);
        System.out.println("상호명: " + tourDTO.getTitle());
        if (tourService.checkContentid(tourDTO.getContentid())) {
            System.out.println("아이디 중복");
            model.addAttribute("errContentid", "이미 사용중인 컨텐츠아이디입니다.");
            return "admin/addTour";
        }
        Tour t = tourDTO.toTourEntity(); // DTO를 Entity로 변환
        tourRepository.save(t);

        System.out.println(city + areacode + sigungucode);
        re.addAttribute("city", city);
        re.addAttribute("areacode", areacode);
        re.addAttribute("sigungucode", sigungucode);
        return "redirect:/category";
    }

    @GetMapping("deleteTour/{id}")
    public String deleteTour(@PathVariable("id") Long id,
                             @RequestParam("city") String city,
                             @RequestParam("areacode") String areacode,
                             @RequestParam("sigungucode") String sigungucode,
                             RedirectAttributes re) {
        System.out.println("id: " + id + " city: " + city + " areacode: " + areacode + " sigungucode: " + sigungucode);
        tourService.delete(id);
        re.addAttribute("city", city);
        re.addAttribute("areacode", areacode);
        re.addAttribute("sigungucode", sigungucode);
        System.out.println(city + areacode + "/" + sigungucode);
        return "redirect:/category";
    }

    @GetMapping("/category")
    public String category2(@PageableDefault(page = 0, size = 12, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                            @RequestParam("city") String city,
                            @RequestParam("areacode") String areacode,
                            @RequestParam("sigungucode") String sigungucode,
                            Model model) {

        Page<Tour>  tlist = tourService.findRegion(areacode, sigungucode, pageable);


        int nowPage = tlist.getNumber(); // 현재 페이지 번호 (0부터 시작)
        int totalPages = tlist.getTotalPages(); // 전체 페이지 수
        int blockLimit = 5; // 페이지 블럭 크기
        int currentBlock = nowPage / blockLimit;

        int startPage = currentBlock * blockLimit ; // ex) 0, 5, 10
        int endPage = Math.min(startPage + blockLimit -1, totalPages-1);
        if(totalPages ==0){
            endPage = 0;
        }
        model.addAttribute("city", city);
        model.addAttribute("areacode", areacode);
        model.addAttribute("sigungucode", sigungucode);
        model.addAttribute("tlist", tlist);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPages", totalPages);

        System.out.println("/category 컨트롤러 실행됨, areacode: "+areacode+" sugungucode: "+sigungucode);

        return "tour/city";
    }

    @GetMapping("/searchTour")
    public String searchTour(@RequestParam("contentid") String contentid, Model model) {
        System.out.println("관광지 아이디 검색");
        return "admin/tourList";
    }

    @GetMapping("checkContentid")
    public ResponseEntity<?> checkId(@RequestParam(value = "contentid", required = false) String contentid) throws BadRequestException {
        System.out.println(contentid);
        if (tourService.checkContentid(contentid)) {
            throw new BadRequestException("이미 사용중인 컨텐츠아이디 입니다.");
        } else {
            return ResponseEntity.ok("사용 가능한 컨텐츠아이디 입니다.");
        }
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
        model.addAttribute("bestFive", bestFive);
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
    public String categoryV(@PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                            @RequestParam("num") String contenttypeid,
                            @RequestParam("areacode") String areacode,
                            @RequestParam("sigungucode") String sigungucode,
                            @RequestParam("city") String city,
                            Model model) {
        Page<Tour> tlist = tourService.findCatecory(contenttypeid, areacode, sigungucode,pageable);
        int nowPage = tlist.getNumber(); // 현재 페이지 번호 (0부터 시작)
        int totalPages = tlist.getTotalPages(); // 전체 페이지 수
        int blockLimit = 5; // 페이지 블럭 크기
        int currentBlock = nowPage / blockLimit;

        int startPage = currentBlock * blockLimit ; // ex) 0, 5, 10
        int endPage = Math.min(startPage + blockLimit -1, totalPages-1);
        if(totalPages ==0){
            endPage = 0;
        }
        model.addAttribute("tlist", tlist);
        model.addAttribute("city", city);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPages", totalPages);
        return "tour/city :: categoryTableFragment";
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

    // 검색
    @GetMapping("/listTour")
    public String searchUser(@RequestParam("title") String title, Model model) {
        List<Tour> list = tourService.findByTitleContaining(title);
        if (title == null) {
            return "tour/listTour";
        }
        if (!list.isEmpty()) {
            System.out.println("검색어 존재");
            model.addAttribute("list", list);
            model.addAttribute("title", title);
            return "tour/listTour"; // 위에서 만든 Thymeleaf 파일명
        }else{
            System.out.println("검색어 없음");
            model.addAttribute("error", "해당 검색어가 존재하지 않습니다.");
            model.addAttribute("title", title);
            return "tour/listTour";
        }
    }



}

