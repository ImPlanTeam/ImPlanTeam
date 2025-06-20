package What2Do.domain;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Data
@Getter
@Setter
@NoArgsConstructor
public class TourDTO {

    private String addr1;//주소
    private String addr2;//상세주소
    private String areacode; //지역코드
    private String cat1; //대분류코드
    private String cat2; //중분류코드
    private String cat3; //소분류코드
    private String contenttypeid; //콘텐츠타입id
    private String firstimage; //대표이미지
    private String firstimage2; //썸네일
    private double mapx; //GPS X좌표
    private double mapy; //GPS Y좌표
    private String sigungucode; //시군구 코드
    private String title; //제목
    private String contentid;
    private int like_count;
    private String overview;

    public Tour toTourEntity(){
        Tour tour = new Tour();
        tour.setContentid(contentid);
        tour.setSigungucode(sigungucode);
        tour.setAddr1(addr1);
        tour.setAddr2(addr2);
        tour.setOverview(overview);
        tour.setFirstimage(firstimage);
        tour.setFirstimage2(firstimage2);
        tour.setCat1(cat1);
        tour.setCat2(cat2);
        tour.setCat3(cat3);
        tour.setTitle(title);
        tour.setMapx(mapx);
        tour.setMapy(mapy);
        tour.setContenttypeid(contenttypeid);
        tour.setAreacode(areacode);
        return tour;
    }
}