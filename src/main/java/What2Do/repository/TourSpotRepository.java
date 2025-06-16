package What2Do.repository;

import What2Do.domain.Tour;
import What2Do.domain.TourSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TourSpotRepository extends JpaRepository<TourSpot, Long> {


}
