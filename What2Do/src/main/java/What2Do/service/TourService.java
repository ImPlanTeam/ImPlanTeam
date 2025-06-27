package What2Do.service;

import What2Do.domain.*;
import What2Do.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.*;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TourService {

    private final TourRepository tourRepository;
    private final TourSpotRepository tourSpotRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private static final Logger logger = LoggerFactory.getLogger(TourService.class);

    @Transactional
    public void saveAllTour(List<Tour> tourList) {

        tourRepository.saveAll(tourList);
        logger.info("관광지 정보 {}건 저장됨", tourList.size());
    }
    public List<String> findC(){
        return tourRepository.findAllByContenttypeid();
    }
    @Transactional
    public void saveAllTour2(List<TourSpot> tourList) {
        tourSpotRepository.saveAll(tourList);
        logger.info("관광지 정보 {}건 저장됨", tourList.size());
    }
    private void validateTour(Tour tour) {
        // 필수 필드 체크
        if (tour.getAddr1() == null || tour.getAddr1().isEmpty()) {
            logger.error("관광지명은 필수입니다: {}", tour);
            throw new IllegalArgumentException("관광지명은 필수입니다.");
        }
        if (tour.getAreacode() == null || tour.getAreacode().isEmpty()) {
            logger.error("관광지구분은 필수입니다: {}", tour);
            throw new IllegalArgumentException("관광지구분은 필수입니다.");
        }
    }

    public Page<Tour> findRegion(String areacode, String sigungucode, Pageable pageable){
        return tourRepository.findByAreacodeAndSigungucode(areacode,sigungucode,pageable);
    }
    public Tour findOne(Long id){
        return tourRepository.findById(id).orElseThrow();
    }

    public Page<Tour> findCatecory(String contenttypeid,String areacode,String sigungucode,Pageable pageable){
        return tourRepository.findByContenttypeidAndAreacodeAndSigungucode(contenttypeid,areacode,sigungucode,pageable);
    }
    @Transactional
    public Integer likePlus(Long num){
        Integer likeCnt=tourRepository.updateLikePlus(num);
        return likeCnt;
    }
    @Transactional
    public Integer likeMinus(Long num){
        Integer likeCnt=tourRepository.updateLikeMinus(num);
        return likeCnt;
    }
    //게시글에 좋아요를 했는지 판별
    public boolean likeB(Long num, String id){
        boolean likeCnt=likeRepository.existsByUserIdAndTourId(id,num);
        return likeCnt;
    }
    public void likeI(Long num,String id){
        User user = userRepository.findById(id);
        Tour tour = tourRepository.findById(num).orElseThrow();
        LikeIt likeIt = new LikeIt();
        likeIt.setUser(user);
        likeIt.setTour(tour);
        likeRepository.save(likeIt);
    }
    public List<Tour> findBest(){
        return tourRepository.findAllOrderByLikecountDesc();
    }


    public void likeD(Long num,String id){
        likeRepository.deleteByTourIdAndUserId(num,id);
    }
    public Tour view(Long num){
        Tour tour = tourRepository.findById(num).orElseThrow();
        return tour;
    }
    public List<Tour> myPage(@RequestParam("userId")String userId){
        return likeRepository.findLikedTourByUserId(userId);
    }

    public List<Tour> findAll(){
        List<Tour> list = tourRepository.findAll(); //관광지 정보를 모두 넘겨주자.
        return  list;
    }

    @Transactional
    public void delete(Long id){
        likeRepository.deleteByTourId(id);
        commentRepository.deleteByTourId(id);
        tourRepository.deleteById(id);
    }

    @Transactional
    public boolean checkContentid(String contentid){
        return tourRepository.existsBycontentid(contentid);
    }

    // 유사 관광지 추천 (Flask 호출)
    public List<Map<String, Object>> getRecommendedToursFromFlask(Tour tour) {
        RestTemplate restTemplate = new RestTemplate();
        String flaskUrl = "http://localhost:7000/recommend";

        //Flask에 보낼 json 데이터
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("overview", tour.getOverview());
        requestData.put("sigungu_code", tour.getSigungucode());
        requestData.put("areacode", tour.getAreacode());
        requestData.put("content_id", tour.getContentid());
        // json전송 헤더설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestData, headers);
        //flask로 post방식 전송
        try {
            System.out.println("Flask에 전송할 데이터: " + requestData);
            ResponseEntity<List> response = restTemplate.exchange(
                    flaskUrl,
                    HttpMethod.POST,
                    entity,
                    List.class
            );
            return response.getBody();
        } catch (Exception e) {
            System.out.println("Flask 서버 호출 실패: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Tour> findByTitleContaining(String title) {
        List<Tour> list= tourRepository.findByTitleContaining(title);
        return list;
    }


}


