package com.Implan.What2Do.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "askfile")
public class AskFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;
    private String filename;        // 원본 파일명
    private String storedFilename;  // 서버에 저장된 파일명
    private String filepath;        // 경로

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ask_no")
    private Ask ask;

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getStoredFilename() {
        return storedFilename;
    }

    public void setStoredFilename(String storedFilename) {
        this.storedFilename = storedFilename;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public Ask getAsk() {
        return ask;
    }

    public void setAsk(Ask ask) {
        this.ask = ask;
    }

}