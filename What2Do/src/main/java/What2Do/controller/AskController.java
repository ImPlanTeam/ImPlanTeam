package What2Do.controller;

import What2Do.domain.Answer;
import What2Do.domain.Ask;
import What2Do.domain.AskFile;
import What2Do.domain.User;
import What2Do.repository.AnswerRepository;
import What2Do.repository.AskRepository;
import What2Do.service.AskService;
import jakarta.servlet.http.HttpSession;
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
                              @RequestParam("files")List<MultipartFile> files,
                              HttpSession session){
        User loginUser = (User) session.getAttribute("user");
        if(loginUser == null){
            return "redirect:/login2";

        }
        try {
            askService.saveAsk(title, content, vicibility, files, loginUser);
        } catch (IOException e) {
            e.printStackTrace();
            return "error"; // 에러 페이지
        }
        return "redirect:/ask";
    }

    //1:1문의 리스트
    @GetMapping("/ask")
    public String allask(Model model, HttpSession session ,@PageableDefault(page=0, size = 10, sort ="no", direction = Sort.Direction.DESC ) Pageable pageable){
        //유저 별 문의목록
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login2";
        }
        if (isAdmin(user)) {
            return "redirect:/admin/ask";
        }

        //리스트출력(페이징처리)
        Page<Ask> alist;

        if (isAdmin(user)) {
            alist = askService.findAsk(pageable); // 관리자만 전체 목록
        } else {
            alist = askService.findAskByUser(user, pageable); // 일반 유저는 자기 글만
        }


        int nowPage = alist.getNumber(); // 현재 페이지 번호 (0부터 시작)
        int totalPages = alist.getTotalPages(); // 전체 페이지 수

        int blockLimit = 5; // 페이지 블럭 크기
        int currentBlock = nowPage / blockLimit;

        int startPage = currentBlock * blockLimit; // ex) 0, 5, 10
        int endPage = Math.min(startPage + blockLimit - 1, totalPages -1);
        if(totalPages ==0){
            endPage = 0;
        }

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
    public String viewDetail(@RequestParam("askNo") Integer askNo, Model model, HttpSession session){
        Ask view = askService.viewDetail(askNo);
        User loginUser = (User) session.getAttribute("user");
        model.addAttribute("askview", view);

        if ("private".equals(view.getVicibility())) {
            if (loginUser == null || !view.getUser().getId().equals(loginUser.getId())) {
                // 관리자 확인 로직 필요 시 추가
                if (!isAdmin(loginUser)) {
                    return "redirect:/ask?error=unauthorized";
                }
            }
        }

        //첨부파일 가져오기
        List<AskFile> imglist = askService.getimg(askNo);
        model.addAttribute("file", imglist);

        Answer answer = answerRepository.findByAsk(view);
        model.addAttribute("answer", answer);

        return "ask/askview";
    }

    private boolean isAdmin(User user) {
        return user != null && user.getRole().equalsIgnoreCase("ADMIN");
    }

    //답변저장
    @PostMapping("/ask/answer/{askNo}")
    public String addAnswer(@PathVariable Integer askNo,
                            @RequestParam String content,
                            HttpSession session) {
        User loginUser = (User) session.getAttribute("user");
        if (!isAdmin(loginUser)) {
            return "redirect:/ask?error=noPermission";
        }

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
    //관리자전용 리스트출력
    @GetMapping("/admin/ask")
    public String adminAskList(Model model, HttpSession session,
                               @PageableDefault(size = 10, sort = "no", direction = Sort.Direction.DESC) Pageable pageable) {
        User user = (User) session.getAttribute("user");
        if (user == null || !isAdmin(user)) {
            return "redirect:/?error=forbidden";
        }

        Page<Ask> asks = askService.findAsk(pageable); // 전체 조회
        model.addAttribute("list", asks);

        int nowPage = asks.getNumber(); // 현재 페이지 번호
        int totalPages = asks.getTotalPages(); // 전체 페이지 수
        int blockLimit = 5; // 한 블럭에 보여줄 페이지 수
        int currentBlock = nowPage / blockLimit;

        int startPage = currentBlock * blockLimit;
        int endPage = Math.min(startPage + blockLimit - 1, totalPages - 1);

        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPages", totalPages);

        // 상태 출력
        Map<Integer, String> answerStatusMap = new HashMap<>();
        for (Ask ask : asks) {
            Answer answer = answerRepository.findByAsk(ask);
            answerStatusMap.put(ask.getNo(), (answer != null) ? "답변완료" : "검토중");
        }
        model.addAttribute("statusMap", answerStatusMap);

        return "admin/ask";
    }

    // 관리자답변 출력 화면
    @GetMapping("/admin/answer/{askNo}")
    public String showAnswerForm(@PathVariable Integer askNo, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !isAdmin(user)) {
            return "redirect:/?error=unauthorized";
        }

        Ask ask = askService.viewDetail(askNo);
        List<AskFile> imglist = askService.getimg(askNo);
        model.addAttribute("askview", ask);
        model.addAttribute("file", imglist);

        // 이미 답변이 달린 경우 처리할 수도 있음
        Answer answer = answerRepository.findByAsk(ask);
        model.addAttribute("answer", answer);

        return "admin/askanswer"; // 관리자용 답변 작성 페이지
    }

    //관리자답변저장
    @PostMapping("/admin/answer/{askNo}")
    public String adminAnswer(@PathVariable Integer askNo,
                            @RequestParam String content,
                            HttpSession session) {
        User loginUser = (User) session.getAttribute("user");
        if (!isAdmin(loginUser)) {
            return "redirect:/admin?error=noPermission";
        }

        askService.saveAnswer(askNo, content);
        return "redirect:/admin/answer/" + askNo;
    }



}