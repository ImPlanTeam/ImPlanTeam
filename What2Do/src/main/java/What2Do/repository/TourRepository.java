package What2Do.repository;

import What2Do.domain.Comment;
import What2Do.domain.LikeIt;
import What2Do.domain.Tour;
import What2Do.domain.TourSpot;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Long> {
    List<Tour> findByAreacodeAndSigungucode(String areacode,String sigungucode);

    List<Tour> findByContenttypeidAndAreacodeAndSigungucode(String contenttypeid,String areacode,String sigungucode);

    @Modifying
    @Transactional
    @Query("update Tour t set t.like_count=t.like_count+1 where t.id=:num")
    Integer updateLikePlus(@Param("num") Long num);

    @Modifying
    @Transactional
    @Query("update Tour t set t.like_count=t.like_count-1 where t.id=:num")
    Integer updateLikeMinus(@Param("num") Long num);

    @Query(value = "SELECT * FROM tour WHERE (overview IS NULL OR overview = '') LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Tour> findOverviewEmptyPaged(@Param("offset") int offset, @Param("limit") int limit);

    @Modifying
    @Transactional
    @Query("select t from Tour t order by t.like_count desc Limit 5")
    List<Tour> findAllOrderByLikecountDesc();

    @Modifying
    @Transactional
    @Query("select t.contentid from Tour t where t.contenttypeid='12'")
    List<String> findAllByContenttypeid();
    public interface TourSpotRepository extends JpaRepository<TourSpot, Long> {


    }
}

