package com.Implan.What2Do.domain;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ask")
public class Ask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ask_no")
    private Integer no;
    private String title;
    private String contents;
    private String vicibility;
    private LocalDateTime indate;

    private String answer;
    private LocalDateTime answerdate;


    @OneToMany(mappedBy = "ask", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AskFile> askFiles = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.indate = LocalDateTime.now();
    }

    public void addFile(AskFile file) {
        askFiles.add(file);
        file.setAsk(this);
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getVicibility() {
        return vicibility;
    }

    public void setVicibility(String vicibility) {
        this.vicibility = vicibility;
    }

    public LocalDateTime getIndate() {
        return indate;
    }

    public void setIndate(LocalDateTime indate) {
        this.indate = indate;
    }

    public List<AskFile> getAskFiles() {
        return askFiles;
    }

    public void setAskFiles(List<AskFile> askFiles) {
        this.askFiles = askFiles;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public LocalDateTime getAnswerdate() {
        return answerdate;
    }

    public void setAnswerdate(LocalDateTime answerdate) {
        this.answerdate = answerdate;
    }
}
