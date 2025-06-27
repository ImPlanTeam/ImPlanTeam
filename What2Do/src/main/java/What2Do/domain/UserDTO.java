package What2Do.domain;

import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.Transient;

import java.time.LocalDateTime;


@Data
@Getter
@Setter
@NoArgsConstructor
public class UserDTO { //클라이언트가 회원가입 폼에 입력한 데이터를 담는다.

    //@NotBlank(message = "아이디를 입력하세요.")
    private String id;

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}",
            message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    @Size(min = 4, max = 15, message = "비밀번호는 8 ~ 16자 사이로 입력해주세요")
    private String pass;

    @Transient
    @NotBlank(message = "비밀번호를 확인하세요.")
    private String pass2;

    @AssertTrue(message = "비밀번호가 일치하지 않습니다.")
    public boolean PasswordMatching() {
        return pass != null && pass.equals(pass2);
    }

//    @NotBlank(message = "닉네임을 입력하세요")
    private String name;

//    @Email(message = "올바른 이메일 주소를 입력해주세요.")
    private String mail;

    private String mail1;
    private String mail2;

//    @Pattern(regexp = "^010-\\d{3,4}-\\d{4}$",
//            message = "전화번호는 010으로 시작하는 11자리 숫자와 '-'로 구성되어야 합니다.")
    private String tel;

    private String jender;

    private String birth;

    private LocalDateTime indate;

    private String checkId;

    private String checkPass;

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getCheckPass() {
        return checkPass;
    }

    public void setCheckPass(String checkPass) {
        this.checkPass = checkPass;
    }

    public LocalDateTime getIndate() {
        return indate;
    }

    public void setIndate(LocalDateTime indate) {
        this.indate = indate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPass2() {
        return pass2;
    }

    public void setPass2(String pass2) {
        this.pass2 = pass2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getJender() {
        return jender;
    }

    public void setJender(String jender) {
        this.jender = jender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getMail1() {
        return mail1;
    }

    public void setMail1(String mail1) {
        this.mail1 = mail1;
    }

    public String getMail2() {
        return mail2;
    }

    public void setMail2(String mail2) {
        this.mail2 = mail2;
    }

    public User toEntity() {
        User user = new User();
        user.setName(this.name);
        user.setPass(this.pass); // 비밀번호는 보통 암호화 필요
        user.setMail(this.mail1+"@"+this.mail2);
        user.setId(this.id);
        user.setBirth(this.birth);
        user.setJender(this.jender);
        user.setTel(this.tel);
        return user;
    }



}
