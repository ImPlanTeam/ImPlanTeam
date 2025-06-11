package What2Do.controller;


import What2Do.domain.Board;
import What2Do.domain.User;
import What2Do.domain.UserDTO;
import What2Do.repository.UserRepository;
import What2Do.service.BoardService;
import What2Do.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.websocket.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.apache.coyote.BadRequestException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class MemberController {


    private final UserRepository userRepository;
    private final BoardService boardService;

    @Autowired
    private UserService userService;

    public MemberController(UserRepository userRepository, BoardService boardService) {
        this.userRepository = userRepository;
        this.boardService = boardService;
    }


    @Autowired
    private UserService service;

    @RequestMapping("myPage")
    public String mp(){
        return "member/myPage";
    }

    @RequestMapping("myInfo")
    public String myInfo(){
        return "member/myInfo";
    }

    @RequestMapping("findMyId")
    public String findMyId(){
        return "member/findMyId";
    }


    @PostMapping("findMyId2")
    public String findMyId2(@RequestParam("name")String name,
                            @RequestParam("tel")String tel, Model model){
        User user = userService.findByTel(tel);
        System.out.println("아이디찾기"+tel+name);
        if (user != null && user.getName().equals(name)) {
            System.out.println(user.getName()+"님");
            model.addAttribute("id", "아이디: " + user.getId());
            System.out.println("아이디"+user.getId());
        }
        else{
            model.addAttribute("id", "아이디를 찾을 수 없습니다.");
        }
        return "member/findMyId";
    }

    @PostMapping("findMyPass2")
    public String findMyPass2(@RequestParam("id") String id,
                              @RequestParam("mail") String mail, Model model){
        User user = userService.findById(id);
        System.out.println("비번찾기"+id+mail);
        if (user != null && user.getMail().equals(mail)) {
            System.out.println(user.getName()+"님");
            model.addAttribute("pass", "비밀번호: " + user.getPass());
            model.addAttribute("id", user.getId());
            System.out.println("비밀번호"+user.getPass());
            return "member/newPass";
        }
        else{
            model.addAttribute("pass", "아이디를 찾을 수 없습니다.");
        }
        return "member/findMyPass";
    }

    @RequestMapping("findMyPass")
    public String findMyPass(){

        return "member/findMyPass";
    }

    @GetMapping("newPass")
    public String newPass(Model model){
        return "member/newPass";
    }

    @PostMapping("/newPass2/{id}")
    public String newPass2(@PathVariable String id,
                           @RequestParam String pass,
                           @RequestParam String pass2,
                           Model model){
        System.out.println(id+pass);
        if (!pass.equals(pass2)){
            System.out.println("err");
            model.addAttribute("errPass", "비밀번호가 일치하지 않습니다.");
            return "member/newPass";
        }else {
            User user = userRepository.findById(id);
            user.setPass(pass);
            userRepository.save(user);
            return "member/login2";
        }
    }

    //유저정보 수정
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable String id,
                             @RequestParam String name,
                             @RequestParam String mail,
                             @RequestParam String tel,
                             @RequestParam String pass,
                             @RequestParam String birth,
                             @RequestParam(name = "jender", required = false)String jender) {
        User user = userRepository.findById(id);
        user.setName(name);
        user.setMail(mail);
        user.setTel(tel);
        user.setPass(pass);
        user.setJender(jender);
        user.setBirth(birth);

        userRepository.save(user);  // 저장 (추가, 수정 둘 다 가능)

        return "redirect:/viewUser/" + id;  // 수정 완료 후 상세보기로 리다이렉트
    }

    @PostMapping("save2")
    public String save2(@Valid UserDTO userDTO, Errors errors, Model model ){
        model.addAttribute("user", userDTO);
        if (service.checkId(userDTO.getId())){
            model.addAttribute("errID", "이미 사용중인 아이디입니다.");
            return "member/join";
        }
        if (service.checkTel(userDTO.getTel())){
            model.addAttribute("errTel", "이미 사용중인 전화번호입니다.");
            return "member/join";
        }
        if (service.checkMail(userDTO.getMail1()+"@"+userDTO.getMail2())){
            model.addAttribute("errMail", "이미 사용중인 이메일입니다.");
            return "member/join";
        }
        if (!Objects.equals(userDTO.getPass(), userDTO.getPass2())){
            model.addAttribute("errPass", "비밀번호가 일치하지 않습니다.");
            return "member/join";
        }
        if (errors.hasErrors()){
            System.out.println("유효성 검사 실패: " + errors.getAllErrors());
            List<String> errorMessages = errors.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            model.addAttribute("errList", errorMessages);

            Map<String, String> result = service.validateHandling(errors);
            System.out.println("회원가입 실패");

            for (String key : result.keySet()){
                model.addAttribute(key, result.get(key));
            }
            return "member/join";
        }
        User u = userDTO.toEntity(); // DTO를 Entity로 변환
        if ("1111".equals(userDTO.getId())) {
            u.setRole("1111");
        } else {
            u.setRole("USER");
        }
        service.save(u);
        System.out.println("회원가입 성공");
        return "redirect:/login2";
    }

    @RequestMapping("list")
    public String list2(Model model){
        List<User> list = service.findAll();
        model.addAttribute("list", list);
        return "admin/memberlist";
    }

    @GetMapping("checkId")
    public ResponseEntity<?> checkId(@RequestParam(value = "id", required = false)String id)throws BadRequestException{
        System.out.println(id);
        if (service.checkId(id)){
            throw new BadRequestException("이미 사용중인 아이디 입니다.");
        }else {
            return ResponseEntity.ok("사용 가능한 아이디 입니다.");
        }
    }

    //삭제
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable String id) {
        boardService.del2(id);
        service.deleteUser(id);
        return "redirect:/list";
    }

    //삭제
    @GetMapping("/delete2/{id}")
    public String deleteUser2(@PathVariable String id) {
        boardService.del2(id);
        service.deleteUser(id);
        return "redirect:/logout";
    }

    //검색
    @GetMapping("/searchUser")
    public String searchUser(@RequestParam("id") String id, Model model) {
        System.out.println("유저검색");
        List<User> list = service.findByIdContaining(id);
        if (id == null) {
            return "admin/memberlist";
        }if (list != null) {
            model.addAttribute("list", list);
            return "admin/memberlist";  // 위에서 만든 Thymeleaf 파일명
        }else{
            model.addAttribute("error", "해당 아이디가 존재하지 않습니다.");
            return "admin/memberlist";
        }
    }

    //유저검색결과
    @GetMapping("/list2/{id}")
    public String l2(@PathVariable String id, Model model) {
        User user = userRepository.findById(id);
        if (user == null) {
            // 없으면 에러 페이지나 404 처리
            return "error/404";
        }
        model.addAttribute("list", user);
        return "admin/memberlist";  // 위에서 만든 Thymeleaf 파일명
    }

    //유저 상세보기
    @GetMapping("/viewUser/{id}")
    public String editUserForm(@PathVariable String id, Model model) {
        User user = userRepository.findById(id);
        if (user == null) {
            // 없으면 에러 페이지나 404 처리
            return "error/404";
        }
        model.addAttribute("user", user);
        return "admin/viewUser";  // 위에서 만든 Thymeleaf 파일명
    }

    //유저 상세보기
    @GetMapping("/viewUser2/{id}")
    public String editUserForm2(@PathVariable String id, Model model) {
        User user = userRepository.findById(id);
        if (user == null) {
            // 없으면 에러 페이지나 404 처리
            return "error/404";
        }
        model.addAttribute("user", user);
        return "member/viewUser2";  // 위에서 만든 Thymeleaf 파일명
    }
    @PostMapping("update2/{id}")
    public String updateUser2(@PathVariable String id,
                              @Valid UserDTO userDTO, Errors errors,
                              @RequestParam String pass2,
                              Model model) {

        User user = userRepository.findById(id);

        if (!userDTO.getPass().equals(pass2)) {
            model.addAttribute("errPass2", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("user", user);
            return "member/viewUser2";
        }

        if (errors.hasErrors()) {
            System.out.println("유효성 검사 실패: " + errors.getAllErrors());
            List<String> errorMessages = errors.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            model.addAttribute("errList", errorMessages);
            model.addAttribute("user", user); // ← 이거 꼭 추가!
            return "member/viewUser2";
        }

        user.setMail(userDTO.getMail());
        user.setPass(userDTO.getPass());
        userRepository.save(user);

        return "redirect:/viewUser2/" + id;
    }

}