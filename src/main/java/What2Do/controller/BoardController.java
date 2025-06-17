package What2Do.controller;


import What2Do.domain.Board;
import What2Do.domain.BoardFile;
import What2Do.domain.Tour;
import What2Do.domain.User;
import What2Do.service.BoardService;
import What2Do.service.TourService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@Controller

public class BoardController {

    @Autowired
    private final BoardService boardService;
    private final TourService tourService;
    private final EntityManager entityManager;

    public BoardController(BoardService boardService, TourService tourService, EntityManager entityManager) {
        this.boardService = boardService;
        this.tourService = tourService;
        this.entityManager = entityManager;
    }


    @GetMapping("/board")
    public String writing() {
        return "board/main";
    }

    //자유게시판 글 등록 기능
    @PostMapping("/register")
    public String register(@RequestParam String title,
                           @RequestParam String content,
                           HttpSession session,
                           @RequestParam String area,
                           @RequestParam List<MultipartFile> files) throws IOException {
        User user = (User) session.getAttribute("user"); // 올바른 캐스팅
        String writer = user.getId(); // 또는 필요한 속성으로 변경
        boardService.register(title, content, writer, area, files);
        return "redirect:/listView";
    }

    //등록된 자유게시판 글 리스트(페이지 포함)
    @GetMapping("/listView")
    public String listView(Model model,
                           @PageableDefault(page = 0, size = 10, sort = "num", direction = Sort.Direction.DESC) Pageable pageable,
                           String searchKeyword) {

        Page<Board> list = null;

        if (searchKeyword == null) {
            list = boardService.listV(pageable);
        } else {
            if (searchKeyword.equals("전체보기")) {
                return "redirect:/listView";
            } else {
                list = boardService.listSearch(pageable, searchKeyword);
            }
        }

        int nowPage = list.getNumber(); // 현재 페이지 번호 (0부터 시작)
        int totalPages = list.getTotalPages(); // 전체 페이지 수
        int blockLimit = 5; // 페이지 블럭 크기
        int currentBlock = nowPage / blockLimit;

        int startPage = currentBlock * blockLimit ; // ex) 0, 5, 10
        int endPage = Math.min(startPage + blockLimit -1, totalPages-1);
        if(totalPages ==0){
            endPage = 0;
        }

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPages", totalPages);


        return "board/listView";
    }

    //셀렉으로 지역 검색한 자유게시판 글 리스트(페이지 포함)
    @GetMapping("/listView2")
    public String listView2(Model model,
                           @PageableDefault(page = 0, size = 10, sort = "num", direction = Sort.Direction.DESC) Pageable pageable,
                           String search) {

        Page<Board> list = null;

        if (search == null) {
            list = boardService.listV(pageable);
        } else {
            if (search.equals("전체보기")) {
                return "redirect:/listView";
            } else {
                list = boardService.listSearch2(pageable, search);
            }
        }

        int nowPage = list.getNumber(); // 현재 페이지 번호 (0부터 시작)
        int totalPages = list.getTotalPages(); // 전체 페이지 수
        int blockLimit = 5; // 페이지 블럭 크기
        int currentBlock = nowPage / blockLimit;

        int startPage = currentBlock * blockLimit; // ex) 0, 5, 10
        int endPage = Math.min(startPage + blockLimit - 1, totalPages);
        if(totalPages ==0){
            endPage = 0;
        }

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPages", totalPages);

        return "board/listView";
    }

    //글 자세히 보기
    @GetMapping("/view")
    public String view(@RequestParam("num") Integer num, Model model,HttpSession session) {
        User user = (User) session.getAttribute("user");
        String id = user.getId();
        boardService.updateCount(num);
        Board board = boardService.view(num);
        List<BoardFile> boardFiles = boardService.viewF(num);
        boolean like = boardService.likeB(num, id);
        model.addAttribute("board", board);
        model.addAttribute("file", boardFiles);
        model.addAttribute("like",like);

        return "board/view";
    }

    @PostMapping("/likeB")
    @ResponseBody
    @Transactional
    public HashMap<String,Object> likeB(@RequestParam("num") Integer num,
                         HttpSession session) {

        User user = (User) session.getAttribute("user");
        String id = user.getId();
        boolean like = boardService.likeB(num, id);
        if (like == false) {
            HashMap<String,Object> likeAll = new HashMap<>();
            boardService.likeI(num, id);
            boardService.likePlus(num);
            entityManager.flush();
            entityManager.clear();
            Board board = boardService.view(num);
            Integer likeCnt = board.getLike_count();
            likeAll.put("likeCnt",likeCnt);
            likeAll.put("like",like);
            return likeAll;
        } else {
            HashMap<String,Object> likeAll = new HashMap<>();
            boardService.likeD(num, id);
            boardService.likeMinus(num);
            Board board = boardService.view(num);
            Integer likeCnt = board.getLike_count();
            likeAll.put("likeCnt",likeCnt);
            likeAll.put("like",like);
            return likeAll;
        }
    }

    //좋아요 누르면 올라가는 기능
//    @PostMapping("/likeIt")
//    @ResponseBody
//    public Integer likeP(@RequestParam("num")Integer num){
//        boardService.likePlus(num);
//        Board board = boardService.view(num);
//        Integer likeCnt=board.getLike_count();
//        return likeCnt;
//    }
//
//    //다시 누르면 좋아요수 마이너스 기능
//    @PostMapping("/likeItM")
//    @ResponseBody
//    public Integer likeM(@RequestParam("num")Integer num){
//        boardService.likeMinus(num);
//        Board board = boardService.view(num);
//        Integer likeCnt=board.getLike_count();
//        return likeCnt;
//    }

    @GetMapping("/delete")
    public String del(Integer num) {
        boardService.del(num);
        return "redirect:/listView";
    }

    @GetMapping("/update")
    public String mod(Model model, @RequestParam("num") Integer num) {
        Board board = boardService.update(num);
        List<BoardFile> boardFiles = boardService.viewF(num);
        model.addAttribute("board", board);
        model.addAttribute("file", boardFiles);
        return "board/mod";
    }

    @PostMapping("/modify")
    public String mod(Board board) {
        System.out.println(board.getNum());
        boardService.modify(board);
        return "redirect:/view?num=" + board.getNum();
    }

    //관리자모드
    @GetMapping("/manager")
    public String listView(Model model,
                           @PageableDefault(page = 0, size = 10, sort = "num", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Board> list = boardService.listV(pageable);

        int nowPage = list.getNumber(); // 현재 페이지 번호 (0부터 시작)
        int totalPages = list.getTotalPages(); // 전체 페이지 수
        int blockLimit = 5; // 페이지 블럭 크기
        int currentBlock = nowPage / blockLimit;

        int startPage = currentBlock * blockLimit; // ex) 0, 5, 10
        int endPage = Math.min(startPage + blockLimit - 1, totalPages - 1);
        if(totalPages ==0){
            endPage = 0;
        }

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPages", totalPages);

        return "admin/b_manager";
    }
    @GetMapping("/deleteM")
    public String delM(@RequestParam("num")Integer num) {
        System.out.println(num);
        boardService.del(num);
        return "redirect:/manager";
    }
    @GetMapping("/myPage")
    public String myPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        String userId = user.getId();// 로그인된 사용자 ID
        List<Board> likedBoards = boardService.myPage(userId);
        model.addAttribute("likedBoards", likedBoards);
        List<Tour> likedTour = tourService.myPage(userId);
        model.addAttribute("likedTour", likedTour);
        List<Board> mine = boardService.searchwriting(userId);
        model.addAttribute("mine", mine);
        return "member/myPage";
    }
    //관리자 글 자세히 보기
    @GetMapping("/bview")
    public String bview(@RequestParam("num") Integer num, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        String id = user.getId();
        boardService.updateCount(num);
        Board board = boardService.view(num);
        List<BoardFile> boardFiles = boardService.viewF(num);
        boolean like = boardService.likeB(num, id);
        model.addAttribute("board", board);
        model.addAttribute("file", boardFiles);
        model.addAttribute("like",like);

        return "admin/b_view";
    }
    //관리자 자유게시판 글 등록 기능
    @PostMapping("/adminregister")
    public String adminregister(@RequestParam String title,
                                @RequestParam String content,
                                HttpSession session,
                                @RequestParam String area,
                                @RequestParam List<MultipartFile> files) throws IOException {
        User user = (User) session.getAttribute("user"); // 올바른 캐스팅
        String writer = user.getName(); // 또는 필요한 속성으로 변경
        boardService.register(title, content, writer, area, files);
        return "redirect:/manager";
    }
    //관리자 글쓰기
    @GetMapping("/board2")
    public String writing2() {
        return "admin/b_main";
    }


}