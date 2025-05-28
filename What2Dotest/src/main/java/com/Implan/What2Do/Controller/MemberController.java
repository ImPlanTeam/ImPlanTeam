package com.Implan.What2Do.Controller;

import com.Implan.What2Do.Service.MemberService;
import com.Implan.What2Do.domain.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원가입 페이지
    @GetMapping("memberjoin")
    public String memberjoin() {

        return "member/memberjoinF";
    }

    // 회원가입 처리
    @PostMapping("/memberjoin")
    public String memberJoinPost(@ModelAttribute Member member) {
        memberService.join(member);
        return "redirect:/index";
    }
    //멤버 리스트조회
    @GetMapping("/allmember")
    public String allmember(Model model){
        List<Member> mlist = memberService.findMembers();
        model.addAttribute("list", mlist);

        return "member/memberlist";
    }
    //자세히보기
    @GetMapping("/viewDetail")
    public String viewDetail(@RequestParam("id") String id, Model model){
        Member view = memberService.viewDetail(id);
        model.addAttribute("memberview", view);
        return "member/memberview";
    }

    //회원삭제기능
    @GetMapping("/del")
    public String deletemember(String id){
        memberService.delete(id);

        return "redirect:/allmember";
    }
    //회원수정 보기
    @GetMapping("/mod")
    public String mod(@RequestParam("id") String id, Model model){
        Member modview = memberService.mod(id);
        model.addAttribute("mod",modview);

        return "member/modm";
    }

    //회원수정 저장
    @PostMapping("/mod")
    public String update(@ModelAttribute Member member) {
        memberService.update(member);
        return "redirect:/allmember";
    }


}
