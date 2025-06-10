package What2Do.config;

public class AreaCode {

    public static final int 서울= 1;
    public static final int 인천= 2;
    public static final int 대전= 3;
    public static final int 대구= 4;
    public static final int 광주= 5;
    public static final int 부산= 6;
    public static final int 울산= 7;
    public static final int 세종= 8;
    public static final int 경기도= 31;
    public static final int 강원도= 32;
    public static final int 충청북도= 33;
    public static final int 충청남도= 34;
    public static final int 경상북도= 35;
    public static final int 경상남도= 36;
    public static final int 전라북도= 37;
    public static final int 전라남도= 38;
    public static final int 제주도= 39;

    public static String getAreaCode(Long areaCode){
        if(areaCode == AreaCode.서울) return "서울";
        else if(areaCode == AreaCode.인천) return "인천";
        else if(areaCode == AreaCode.대전) return "대전";
        else if(areaCode == AreaCode.대구) return "대구";
        else if(areaCode == AreaCode.광주) return "광주";
        else if(areaCode == AreaCode.부산) return "부산";
        else if(areaCode == AreaCode.울산) return "울산";
        else if(areaCode == AreaCode.세종) return "세종";
        else if(areaCode == AreaCode.경기도) return "경기도";
        else if(areaCode == AreaCode.강원도) return "강원도";
        else if(areaCode == AreaCode.충청북도) return "충청북도";
        else if(areaCode == AreaCode.충청남도) return "충청남도";
        else if(areaCode == AreaCode.경상북도) return "경상북도";
        else if(areaCode == AreaCode.경상남도) return "경상남도";
        else if(areaCode == AreaCode.전라북도) return "전라북도";
        else if(areaCode == AreaCode.전라남도) return "전라남도";
        else if(areaCode == AreaCode.제주도) return "제주도";
        else return "기타";




    }


}
