package What2Do.service;

import What2Do.domain.*;
import What2Do.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestParam;

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

    public List<Tour> findRegion(String areacode,String sigungucode){
        return tourRepository.findByAreacodeAndSigungucode(areacode,sigungucode);
    }
    public Tour findOne(Long id){
        return tourRepository.findById(id).orElseThrow();
    }

    public List<Tour> findCatecory(String contenttypeid,String areacode,String sigungucode){

        return tourRepository.findByContenttypeidAndAreacodeAndSigungucode(contenttypeid,areacode,sigungucode);
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

    public Tour findByContentid(String contentid){
        Tour tour = tourRepository.findBycontentid(contentid);
        return tour; //리턴값으로 찾아낸 contentid를 주겠다.
    }

    public List<Tour> findAll(){
        List<Tour> list = tourRepository.findAll(); //관광지 정보를 모두 넘겨주자.
        return  list;
    }

    @Transactional
    public void delete(String contentid){
            tourRepository.deleteBycontentid(contentid);
    }


}
