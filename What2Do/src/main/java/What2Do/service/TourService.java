package What2Do.service;

import What2Do.domain.Tour;
import What2Do.repository.TourRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import org.slf4j.Logger;

@Service
@RequiredArgsConstructor
public class TourService {

    private final TourRepository tourRepository;
    private static final Logger logger = LoggerFactory.getLogger(TourService.class);

    @Transactional
    public void saveAllTour(List<Tour> tourList) {
        for (Tour tour : tourList) {
            validateTour(tour);
        }
        tourRepository.saveAll(tourList);
        logger.info("관광지 정보 {}건 저장됨", tourList.size());
    }

    private void validateTour(Tour tour) {
        // 필수 필드 체크
        if (tour.getFacltnm() == null || tour.getFacltnm().isEmpty()) {
            logger.error("관광지명은 필수입니다: {}", tour);
            throw new IllegalArgumentException("관광지명은 필수입니다.");
        }
        if (tour.getFacltdivnm() == null || tour.getFacltdivnm().isEmpty()) {
            logger.error("관광지구분은 필수입니다: {}", tour);
            throw new IllegalArgumentException("관광지구분은 필수입니다.");
        }
    }
}
