package What2Do.controller;

import What2Do.domain.User;
import What2Do.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login2")
    public String moveToLogin(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/";  // 이미 로그인된 경우 홈 페이지로 리다이렉트
        }
        System.out.println("go to loginform");
        return "/member/login2";
    }

    @GetMapping("/logincheck")
    public String logincheckRedirect() {

        return "redirect:/login2"; // GET 요청이면 로그인 폼으로 보냄
    }

    @PostMapping("/logincheck")
    public String logincheck(String id, String pass, HttpSession session, Model model) {
        User user = userService.findById(id);
        System.out.println("login check");

        if (user != null && user.getPass().equals(pass)) {
            System.out.println(id + " Login");
            System.out.println("로그인 성공. ROLE: " + user.getRole());
            session.setAttribute("user", user);
            model.addAttribute("id", id + "님 어서오세요.");

            // 역할에 따라 분기
                return "redirect:/what2do"; // 일반 유저 메인 페이지

        } else {
            System.out.println("Login failed");
            model.addAttribute("error", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "/member/login2";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, Model model) {
        session.removeAttribute("user");
        session.invalidate();
        System.out.println("logout");
        model.addAttribute("out", "로그아웃 되었습니다.");
        return "redirect:/what2do";
    }
}