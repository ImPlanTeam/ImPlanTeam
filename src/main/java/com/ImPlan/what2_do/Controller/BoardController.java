package com.ImPlan.what2_do.Controller;

import com.ImPlan.what2_do.Domain.Board;
import com.ImPlan.what2_do.Domain.BoardFile;
import com.ImPlan.what2_do.Service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller

public class BoardController {

    @Autowired
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }


    @GetMapping("/board")
    public String writing(){
        return "main";
    }
    //자유게시판 글 등록 기능
    @PostMapping("/register")
    public String register(@RequestParam String title,
                           @RequestParam String content,
                           @RequestParam String writer,
                           @RequestParam String area,
                           @RequestParam List<MultipartFile>files)throws IOException {
        boardService.register(title,content,writer,area,files);
        return "main";
    }

    //등록된 자유게시판 글 리스트(페이지 포함)
    @GetMapping("/listView")
    public String listView(Model model,
                           @PageableDefault(page=0, size = 2, sort ="num", direction = Sort.Direction.DESC ) Pageable pageable,
                           String searchKeyword){

        Page<Board>list = null;

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

        int startPage = currentBlock * blockLimit; // ex) 0, 5, 10
        int endPage = Math.min(startPage + blockLimit - 1, totalPages - 1);


        model.addAttribute("list",list);
        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        model.addAttribute("totalPages", totalPages);


        return "listView";
    }

    //글 자세히 보기
    @GetMapping("/view")
    public String view(@RequestParam("num")Integer num, Model model){
        boardService.updateCount(num);
        Board board = boardService.view(num);
        List<BoardFile> boardFiles = boardService.viewF(num);

        model.addAttribute("board", board);
        model.addAttribute("file",boardFiles);
        return "view";
    }

    //좋아요 누르면 올라가는 기능
    @PostMapping("/likeIt")
    @ResponseBody
    public Integer likeP(@RequestParam("num")Integer num){
        boardService.likePlus(num);
        Board board = boardService.view(num);
        Integer likeCnt=board.getLike_count();
        return likeCnt;
    }

    //다시 누르면 좋아요수 마이너스 기능
    @PostMapping("/likeItM")
    @ResponseBody
    public Integer likeM(@RequestParam("num")Integer num){
        boardService.likeMinus(num);
        Board board = boardService.view(num);
        Integer likeCnt=board.getLike_count();
        return likeCnt;
    }




    /*@PostMapping("/viewCnt")
    @ResponseBody
    public int viewCnt(@RequestParam("num")Integer num){
        boardService.updateCount(num);
        Board board = boardService.view(num);
        int viewCnt=board.getView_count();
        return viewCnt;
    }*/





}
