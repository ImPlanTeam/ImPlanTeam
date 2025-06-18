package What2Do.controller;

import What2Do.domain.Tour;
import What2Do.domain.TourSpot;
import What2Do.repository.TourRepository;
import What2Do.service.TourService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TourController {

    private final TourService tourService;
    private final TourRepository tourRepository;
    private static final Logger logger = LoggerFactory.getLogger(TourController.class);

    @GetMapping("/contentidFind")
    public String contentidFind() {
        List<String> tList = tourService.findC();
        List<TourSpot> tourList = new ArrayList<>();
        StringBuilder result = new StringBuilder();
        for (String contentId : tList) {
            String urlStr ="https://apis.data.go.kr/B551011/KorService2/detailIntro2?" +
                    "serviceKey=HI4uJdHAz5JRb2JVDzardd1U0%2FYqhiVizmMqkHND%2FsE19hTvA3QhWCCbHs0FbiMc%2Bscyz1zQxWkuoreAo6ywRQ%3D%3D" +
                    "&MobileApp=AppTest&MobileOS=ETC&pageNo=1&numOfRows=1&_type=json" +
                    "&contentTypeId=12&contentId=" + contentId;
            System.out.println(urlStr);
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) new URL(urlStr).openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(10000);

                int status = urlConnection.getResponseCode();
                if (status != 200) {
                    logger.warn("Tour API 응답 오류 - 상태코드: {}", status);
                    return "API 호출 실패: 상태코드 " + status;
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) response.append(line);
                br.close();
                urlConnection.disconnect();

                String responseBody = response.toString();
                if (responseBody.startsWith("<")) {
                    logger.warn("HTML 응답 감지 - JSON 파싱 중단");
                    return "API 호출 실패: JSON 응답이 아님";
                }

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode items = objectMapper.readTree(responseBody)
                        .path("response").path("body").path("items").path("item");



                for (JsonNode item : items) {
                    TourSpot tourSpot = new TourSpot();
                    tourSpot.setContentid(item.path("contentid").asText(""));
                    tourSpot.setRestdate(item.path("restdate").asText(""));
                    tourSpot.setInfocenter(item.path("infocenter").asText(""));
                    tourSpot.setUsetime(item.path("usetime").asText(""));
                    tourSpot.setParking(item.path("parking").asText(""));
                    tourSpot.setChkbabycarriage(item.path("chkbabycarriage").asText(""));
                    tourSpot.setChkpet(item.path("chkpet").asText(""));


                    Thread.sleep(300); // 요청 속도 제한 회피

                    tourList.add(tourSpot);
                }



            } catch (Exception e) {
                logger.error("API 호출 중 오류 발생", e);
                return "API 처리 오류: " + e.getMessage();
            }

        }
        tourService.saveAllTour2(tourList);
        return "관광지 정보 저장 완료 (" + tourList.size() + "건)";

    }

    @GetMapping("/api/tour")
    public String callTourApi() {
        StringBuilder result = new StringBuilder();
        String urlStr = "http://apis.data.go.kr/B551011/KorService2/areaBasedList2?" +
                "serviceKey=5o5mT55uPpbzp4BPfEn%2FIToXqXB%2BXCa4WHtf0JZ6UNHRCbmyqNJQsC%2BLdjDbU0NnlrEp04dU69pJF6MAzvy5bw%3D%3D" +
                "&numOfRows=1000&pageNo=1&MobileOS=ETC&MobileApp=TestApp&_type=json";

        try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(urlStr).openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);

            int status = urlConnection.getResponseCode();
            if (status != 200) {
                logger.warn("Tour API 응답 오류 - 상태코드: {}", status);
                return "API 호출 실패: 상태코드 " + status;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) response.append(line);
            br.close();
            urlConnection.disconnect();

            String responseBody = response.toString();
            if (responseBody.startsWith("<")) {
                logger.warn("HTML 응답 감지 - JSON 파싱 중단");
                return "API 호출 실패: JSON 응답이 아님";
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode items = objectMapper.readTree(responseBody)
                    .path("response").path("body").path("items").path("item");

            List<Tour> tourList = new ArrayList<>();

            for (JsonNode item : items) {
                Tour tour = new Tour();
                tour.setAddr1(item.path("addr1").asText(""));
                tour.setAddr2(item.path("addr2").asText(""));
                tour.setAreacode(item.path("areacode").asText(""));
                tour.setCat1(item.path("cat1").asText(""));
                tour.setCat2(item.path("cat2").asText(""));
                tour.setCat3(item.path("cat3").asText(""));
                tour.setContenttypeid(item.path("contenttypeid").asText(""));
                tour.setFirstimage(item.path("firstimage").asText(""));
                tour.setFirstimage2(item.path("firstimage2").asText(""));
                tour.setMapx(item.path("mapx").asDouble());
                tour.setMapy(item.path("mapy").asDouble());
                tour.setSigungucode(item.path("sigungucode").asText(""));
                tour.setTitle(item.path("title").asText(""));
                tour.setContentid(item.path("contentid").asText(""));

                // 상세 정보 요청
                String contentId = tour.getContentid();
                JsonNode detail = fetchDetailInfo(contentId);
                if (detail != null) {
                    tour.setOverview(detail.path("overview").asText(""));
                }

                Thread.sleep(300); // 요청 속도 제한 회피

                tourList.add(tour);
            }

            tourService.saveAllTour(tourList);
            return "관광지 정보 저장 완료 (" + tourList.size() + "건)";

        } catch (Exception e) {
            logger.error("API 호출 중 오류 발생", e);
            return "API 처리 오류: " + e.getMessage();
        }
    }

    @GetMapping("/api/tour/updateOverview")
    public String updateTourOverviews() {
        List<Tour> allTours = tourRepository.findAll();
        int updatedCount = 0;

        for (Tour tour : allTours) {
            try {
                if (tour.getOverview() != null && !tour.getOverview().isBlank()) continue;

                JsonNode detail = fetchDetailInfo(tour.getContentid());
                if (detail != null) {
                    String newOverview = detail.path("overview").asText("");
                    if (!newOverview.isBlank()) {
                        tour.setOverview(newOverview);
                        tourRepository.save(tour);
                        updatedCount++;
                        Thread.sleep(150); // API 요청 딜레이
                    }
                }
            } catch (Exception e) {
                logger.warn("상세정보 갱신 실패 - contentId: {}, 이유: {}", tour.getContentid(), e.getMessage());
            }
        }

        return "상세정보 업데이트 완료. 총 " + updatedCount + "건 업데이트됨.";
    }

    private JsonNode fetchDetailInfo(String contentId) {
        try {
            String urlStr = "https://apis.data.go.kr/B551011/KorService2/detailCommon2?" +
                    "serviceKey=5o5mT55uPpbzp4BPfEn%2FIToXqXB%2BXCa4WHtf0JZ6UNHRCbmyqNJQsC%2BLdjDbU0NnlrEp04dU69pJF6MAzvy5bw%3D%3D" +
                    "&MobileApp=AppTest&MobileOS=ETC&_type=json" +
                    "&contentId=" + contentId;

            HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() != 200) {
                logger.warn("상세 API 상태코드 오류 - contentId: {}, code: {}", contentId, conn.getResponseCode());
                return null;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) result.append(line);
            br.close();
            conn.disconnect();

            if (result.toString().startsWith("<")) {
                logger.warn("HTML 오류 응답 - contentId: {}, 응답: {}", contentId, result.substring(0, 200));
                return null;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(result.toString())
                    .path("response").path("body").path("items").path("item").get(0);

        } catch (Exception e) {
            logger.warn("상세 API 호출 실패 - contentId: {}, 오류: {}", contentId, e.getMessage());
            return null;
        }
    }

    @GetMapping("/api/tour/updateAllOverviews")
    public String updateAllOverviews() {
        int page = 1;
        int pageSize = 100;
        int totalUpdated = 0;

        while (true) {
            int offset = (page - 1) * pageSize;
            List<Tour> targetTours = tourRepository.findOverviewEmptyPaged(offset, pageSize);

            if (targetTours.isEmpty()) {
                break; // 더 이상 처리할 항목이 없으면 종료
            }

            int updatedThisPage = 0;

            for (Tour tour : targetTours) {
                try {
                    JsonNode detail = fetchDetailInfo(tour.getContentid());
                    if (detail != null) {
                        String overview = detail.path("overview").asText("");
                        if (!overview.isBlank()) {
                            tour.setOverview(overview);
                            tourRepository.save(tour);
                            updatedThisPage++;
                            Thread.sleep(150);
                        }
                    }
                } catch (Exception e) {
                    logger.warn("상세정보 실패 - contentId: {}, 이유: {}", tour.getContentid(), e.getMessage());
                }
            }

            logger.info("Page {} 완료 - {}건 업데이트됨", page, updatedThisPage);
            totalUpdated += updatedThisPage;
            page++;
        }

        return "모든 overview 업데이트 완료 - 총 " + totalUpdated + "건 업데이트됨";
    }
}