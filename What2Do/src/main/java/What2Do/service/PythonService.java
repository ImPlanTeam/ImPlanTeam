package What2Do.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PythonService {

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Integer> getTopTourIndexes(List<String> texts) {
        String url = "http://localhost:5000/recommend-tour";  // Flask 서버 주소

        // 요청 JSON 구성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("texts", texts);

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<List> response = restTemplate.postForEntity(url, entity, List.class);
            return response.getBody();  // 결과: ex) [2, 5, 7]
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
