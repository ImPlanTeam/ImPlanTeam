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

    private String facltnm; 	//FACLT_NM 관광지명
    private String facltdivnm;	//FACLT_DIV_NM 관광지구분
    private String refinlotnoaddr;		//REFINE_LOTNO_ADDR 소재지지번주소
    @Column(length = 2000)
    private String touresrtinfo;	//TOURESRT_INFO 관광지 소개
    @Column(length = 2000)
    private String convncefacltinfo;	//CONVNCE_FACLT_INFO 공공편익시설정보
    @Column(length = 2000)
    private String stayngfacltinfo;		//STAYNG_FACLT_INFO 숙박시설정
    @Column(length = 2000)
    private String recratnfacltinfo;	//RECRATN_FACLT_INFO 운동및오락시설정보
    @Column(length = 2000)
    private String culturfacltinfo;		//CULTUR_FACLT_INFO 휴양및문화시설정보
    private Double refinewgs84lat; 	//REFINE_WGS84_LAT 위도
    private Double refinewgs84logt; 	//REFINE_WGS84_LOGT 경도


}
