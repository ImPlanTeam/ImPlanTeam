package What2Do.controller;


import What2Do.domain.User;
import What2Do.domain.UserDTO;
import What2Do.repository.UserRepository;
import What2Do.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.apache.coyote.BadRequestException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class MemberController {

    private final UserRepository userRepository;

    public MemberController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private UserService service;

    /*   @PostMapping ("save") //C
       public String save(User u ){
           System.out.println("아이디 : "+u.getId());
           service.save(u);
           return "main";
       }
   */
    @RequestMapping("myPage")
    public String mp(){
        return "member/myPage";
    }

    @PostMapping("save2")
    public String save2(@Valid UserDTO userDTO, Errors errors, Model model ){
        System.out.println("회원가입");
        if (service.checkId(userDTO.getId())){
            model.addAttribute("errID", "이미 사용중인 ID입니다.");
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
            return "/ask/what2do";
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
        service.deleteUser(id);
        return "redirect:/list";
    }

    //삭제
    @GetMapping("/delete2/{id}")
    public String deleteUser2(@PathVariable String id) {
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

    //유저정보 수정
    @PostMapping("/update2/{id}")
    public String updateUser2(@PathVariable String id,
                              @RequestParam String mail,
                              @RequestParam String pass) {
        User user = userRepository.findById(id);
        user.setMail(mail);
        user.setPass(pass);
        userRepository.save(user);  // 저장 (추가, 수정 둘 다 가능)

        return "redirect:/viewUser2/" + id;  // 수정 완료 후 상세보기로 리다이렉트
    }






}
