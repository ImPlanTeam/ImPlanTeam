package What2Do.repository;

import What2Do.domain.Tour;
import What2Do.domain.TourSpot;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Long> {
    Page<Tour> findByAreacodeAndSigungucode(String areacode, String sigungucode, Pageable pageable);


    Page<Tour> findByContenttypeidAndAreacodeAndSigungucode(String contenttypeid,String areacode,String sigungucode,Pageable pageable);

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

    void deleteById(Long id); // 삭제된 개수 반환

    boolean existsBycontentid(String contentid);

    //    파이썬
    // overview가 null이 아니고 "-"가 아닌 관광지만 조회
    @Query("SELECT t FROM Tour t WHERE t.overview IS NOT NULL AND t.overview <> '-'")
    List<Tour> findAllWithValidOverview();

    List<Tour> findByTitleContaining(String title);
}
