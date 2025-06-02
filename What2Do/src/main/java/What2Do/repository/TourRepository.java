package What2Do.repository;

import What2Do.domain.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Long> {
    List<Tour> findByAreacodeAndSigungucode(String areacode,String sigungucode);

    List<Tour> findByContenttypeid(String contenttypeid);

    // contentid 중복 여부 확인용 메서드
    boolean existsByContentid(String contentid);

    @Query(value = "SELECT * FROM tour WHERE (overview IS NULL OR overview = '') LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Tour> findOverviewEmptyPaged(@Param("offset") int offset, @Param("limit") int limit);

    List<Tour> findByContenttypeidAndAreacodeAndSigungucode(String contenttypeid,String areacode,String sigungucode);
}
