package What2Do.service;

import What2Do.domain.Tour;
import What2Do.repository.TourRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;

@Service
@RequiredArgsConstructor
public class TourService {

    private final TourRepository tourRepository;
    private static final Logger logger = LoggerFactory.getLogger(TourService.class);

    @Transactional
    public void saveAllTour(List<Tour> tourList) {
        List<Tour> toSave = new ArrayList<>();

        for (Tour tour : tourList) {
            validateTour(tour);

            if (tour.getOverview() == null || tour.getOverview().isBlank()) {
                logger.warn("상세정보가 없어 저장 제외 - contentId: {}, 제목: {}", tour.getContentid(), tour.getTitle());
                continue;
            }

            if (!tourRepository.existsByContentid(tour.getContentid())) {
                toSave.add(tour);
            } else {
                logger.info("이미 저장된 관광지 건너뜀 - contentId: {}, 제목: {}", tour.getContentid(), tour.getTitle());
            }
        }

        tourRepository.saveAll(toSave);
        logger.info("신규 관광지 {}건 저장 완료", toSave.size());
    }

    private void validateTour(Tour tour) {
        if (tour.getTitle() == null || tour.getTitle().isBlank()) {
            logger.warn("유효하지 않은 Tour 객체 - contentId: {}, 제목 없음", tour.getContentid());
            throw new IllegalArgumentException("관광지명은 필수입니다.");
        }

        if (tour.getMapx() == 0 || tour.getMapy() == 0) {
            logger.warn("좌표 정보 없음 - contentId: {}, 제목: {}", tour.getContentid(), tour.getTitle());
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

}
