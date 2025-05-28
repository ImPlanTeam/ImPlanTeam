package What2Do.controller;

import What2Do.domain.Tour;
import What2Do.service.TourService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TourController {

    private final TourService tourService;
    private static final Logger logger =
            LoggerFactory.getLogger(TourController.class);


    @GetMapping("/api/tour")
    public String callTourApi() {
        StringBuilder result = new StringBuilder();
        // API URL 하드코딩
        String urlStr = "https://openapi.gg.go.kr/TOURESRTINFO?KEY=67cf7d1a2f424502bc98781d6c6ac75b&Type=json&pIndex=1&pSize=1000";
        HttpURLConnection urlConnection = null;
        BufferedReader br = null;
        logger.info("API 원시 응답: " + result.toString());
        try {
            // API 요청 설정
            URL url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(10000); // 10초 연결 타임아웃
            urlConnection.setReadTimeout(10000); // 10초 읽기 타임아웃

            // 응답을 BufferedReader로 읽기
            br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            String returnLine;
            while ((returnLine = br.readLine()) != null) {
                result.append(returnLine).append("\n");
            }

            // JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(result.toString());
            JsonNode itemsNode = rootNode.path("TOURESRTINFO").get(1).path("row");

            logger.info("Items count: " + itemsNode.size());
            System.out.println(itemsNode);

            List<Tour> tourList = new ArrayList<>();
            for (JsonNode itemNode : itemsNode) {
                Tour tour = new Tour();

                // 각 필드에 값 설정
                tour.setFacltnm(itemNode.path("FACLT_NM").asText());
                tour.setFacltdivnm(itemNode.path("FACLT_DIV_NM").asText());
                tour.setRefinlotnoaddr(itemNode.path("REFINE_LOTNO_ADDR").asText());
                tour.setTouresrtinfo(itemNode.path("TOURESRT_INFO").asText());
                tour.setConvncefacltinfo(itemNode.path("CONVNCE_FACLT_INFO").asText());
                tour.setStayngfacltinfo(itemNode.path("STAYNG_FACLT_INFO").asText());
                tour.setRecratnfacltinfo(itemNode.path("RECRATN_FACLT_INFO").asText());
                tour.setCulturfacltinfo(itemNode.path("CULTUR_FACLT_INFO").asText());
                tour.setRefinewgs84lat(itemNode.path("REFINE_WGS84_LAT").asDouble());
                tour.setRefinewgs84logt(itemNode.path("CREFINE_WGS84_LOGT").asDouble());


                logger.info("파싱된 가게: " + tour);

                tourList.add(tour);
            }

            // 데이터베이스에 저장
            tourService.saveAllTour(tourList);
            logger.info("관광지 정보가 성공적으로 저장되었습니다.");

        } catch (IOException e) {
            logger.error("API 호출 중 오류가 발생했습니다: " + e.getMessage());
            logger.error("응답 본문:\n{}", result.toString());
            return "API 호출 중 오류가 발생했습니다: " + e.getMessage();
        } finally {
            // 자원 정리
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    logger.error("BufferedReader 닫기 중 오류 발생: " + e.getMessage());
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return "데이터가 성공적으로 저장되었습니다.";
    }
}