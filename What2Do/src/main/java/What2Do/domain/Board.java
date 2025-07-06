package What2Do.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.List;


@SequenceGenerator(
        name = "num_seq",
        sequenceName = "num_seq",
        initialValue = 1,
        allocationSize = 1
)
@Entity
@Table(name="b_board")
@NoArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "num_seq")
    private Integer num;
    private String title;
    @Column(length = 15000)
    private String content;
    private String writer;
    @ColumnDefault("'0'")
    private int view_count;
    @ColumnDefault("'0'")
    private int like_count;
    @CreationTimestamp
    private String indate;
    private String area;

    @OneToMany(mappedBy = "postnum", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<BoardFile> files=new ArrayList<>();

    public List<BoardFile> getFiles() {
        return files;
    }

    public void setFiles(List<BoardFile> files) {
        this.files = files;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public int getView_count() {
        return view_count;
    }

    public void setView_count(int view_count) {
        this.view_count = view_count;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public String getIndate() {
        return indate;
    }

    public void setIndate(String indate) {
        this.indate = indate;
    }
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }


    public void addFile(BoardFile boardFile) {
        files.add(boardFile);
        boardFile.setPostnum(this);
    }
}
