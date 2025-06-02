package What2Do.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000)
    private String addr1;//주소
    private String addr2;//상세주소
    private String areacode; //지역코드
    private String cat1; //대분류코드
    private String cat2; //중분류코드
    private String cat3; //소분류코드
    private String contenttypeid; //콘텐츠타입id
    @Column(length = 2000)
    private String firstimage; //대표이미지
    @Column(length = 2000)
    private String firstimage2; //썸네일
    private double mapx; //GPS X좌표
    private double mapy; //GPS Y좌표
    @Column(length = 2000)
    private String sigungucode; //시군구 코드
    private String title; //제목
    private String contentid;
    @Column(length = 3000)
    private String overview;    //내용


}
