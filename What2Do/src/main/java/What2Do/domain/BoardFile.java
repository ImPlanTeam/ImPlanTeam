package What2Do.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="b_picture")
public class BoardFile {
    private String filename;

    @ManyToOne //하나의 num은 여러개의 첨부파일을 올릴수 있다
    @JoinColumn(name="num")
    private Board postnum; //게시판번호

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column (name="filenum",insertable=false, updatable=false  )
    private Integer filenum; //파일번호


    private String originFileName;  // 파일 원본명

    private String filePath;  // 파일 저장 경로

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Board getPostnum() {
        return postnum;
    }

    public void setPostnum(Board postnum) {
        this.postnum = postnum;
    }

    public String getOriginFileName() {
        return originFileName;
    }

    public void setOriginFileName(String originFileName) {
        this.originFileName = originFileName;
    }

    public Integer getFilenum() {
        return filenum;
    }

    public void setFilenum(Integer filenum) {
        this.filenum = filenum;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
