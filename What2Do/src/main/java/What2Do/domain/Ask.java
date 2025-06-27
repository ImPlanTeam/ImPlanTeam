package What2Do.domain;




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
    @Column(length = 5000)
    private String contents;
    private LocalDateTime indate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private String answer;
    private LocalDateTime answerdate;
    @OneToOne(mappedBy = "ask", cascade = CascadeType.ALL, orphanRemoval = true)
    private Answer answerEntity;


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

    public Answer getAnswerEntity() {
        return answerEntity;
    }

    public void setAnswerEntity(Answer answerEntity) {
        this.answerEntity = answerEntity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
