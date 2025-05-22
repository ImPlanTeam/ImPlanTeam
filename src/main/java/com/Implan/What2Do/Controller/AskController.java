package com.Implan.What2Do.Controller;

import com.Implan.What2Do.Repository.AnswerRepository;
import com.Implan.What2Do.Repository.AskRepository;
import com.Implan.What2Do.Service.AskService;
import com.Implan.What2Do.domain.Answer;
import com.Implan.What2Do.domain.Ask;
import com.Implan.What2Do.domain.AskFile;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AskController {

    private final AskService askService;
    private final AskRepository askRepository;
    private final AnswerRepository answerRepository;

    @GetMapping("/askjoin")
    public String askjoind(){

        return "ask/askjoinF";
    }
    //1:1문의 등록
    @PostMapping("/askjoin")
    public String askJoinPost(@RequestParam String title,
                              @RequestParam String content,
                              @RequestParam String vicibility,
                              @RequestParam("files")List<MultipartFile> files){
        try {
            askService.saveAsk(title, content, vicibility, files);
        } catch (IOException e) {
            e.printStackTrace();
            return "error"; // 에러 페이지
        }
        return "redirect:/allask";
    }

    //1:1문의 리스트
    @GetMapping("/allask")
    public String allask(Model model, @PageableDefault(page=0, size = 1, sort ="no", direction = Sort.Direction.DESC ) Pageable pageable){
        //리스트출력(페이징처리)
        Page<Ask> alist = askService.findAsk(pageable);


        int nowPage = alist.getNumber(); // 현재 페이지 번호 (0부터 시작)
        int totalPages = alist.getTotalPages(); // 전체 페이지 수

        int blockLimit = 5; // 페이지 블럭 크기
        int currentBlock = nowPage / blockLimit;

        int startPage = currentBlock * blockLimit; // ex) 0, 5, 10
        int endPage = Math.min(startPage + blockLimit - 1, totalPages -1);

        model.addAttribute("list", alist);
        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        model.addAttribute("totalPages", alist.getTotalPages());

        //상태출력
        Map<Integer, String> answerStatusMap = new HashMap<>();
        for (Ask ask : alist) {
            Answer answer = answerRepository.findByAsk(ask);
            answerStatusMap.put(ask.getNo(), (answer != null) ? "답변완료" : "검토중");
        }
        model.addAttribute("statusMap", answerStatusMap);


        return "ask/asklist";
    }

    //1:1문의 자세히보기
    @GetMapping("/viewDetail")
    public String viewDetail(@RequestParam("askNo") Integer askNo, Model model){
        Ask view = askService.viewDetail(askNo);
        model.addAttribute("askview", view);

        //첨부파일 가져오기
        List<AskFile> imglist = askService.getimg(askNo);
        model.addAttribute("file", imglist);

        Answer answer = answerRepository.findByAsk(view);
        model.addAttribute("answer", answer);

        return "ask/askview";
    }

    //답변저장
    @PostMapping("/ask/answer/{askNo}")
    public String addAnswer(@PathVariable Integer askNo, @RequestParam String content) {
        askService.saveAnswer(askNo, content);
        return "redirect:/viewDetail?askNo=" + askNo;
    }

    //답변출력
    @GetMapping("/ask/view/{askNo}")
    public String viewAnswer(@PathVariable Integer askNo, Model model) {
        Ask ask = askRepository.findById(askNo).orElseThrow();
        Answer answer = answerRepository.findByAsk(ask);
        model.addAttribute("ask", ask);
        model.addAttribute("answer", answer);
        return "ask/askview";
    }






}
