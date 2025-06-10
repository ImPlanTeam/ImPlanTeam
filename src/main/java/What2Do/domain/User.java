package What2Do.domain;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")

public class User {

    @Id
    private String id;

    private String name;
    private String mail;
    private String pass;
    private String jender;
    private String tel;
    private String birth;
    private String role;
    @Column(name = "indate")
    private LocalDateTime indate;

    public LocalDateTime getIndate() {
        return indate;
    }

    public void setIndate(LocalDateTime indate) {
        this.indate = indate;
    }
    @PrePersist
    protected void onCreate() {
        this.indate = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Ask> asks = new ArrayList<>();

    public List<Ask> getAsks() {
        return asks;
    }

    public void setAsks(List<Ask> asks) {
        this.asks = asks;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getJender() {
        return jender;
    }

    public void setJender(String jender) {
        this.jender = jender;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }


}
