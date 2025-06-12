package What2Do.repository;

import What2Do.domain.City;
import What2Do.domain.Comment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CityRepository extends JpaRepository<City,Integer> {

    List<City> findByAreaAndCity(String area,String city);

    List<City> findByArea(String area);

}
