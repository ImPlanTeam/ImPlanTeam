package What2Do.repository;


import What2Do.domain.Board;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Integer> {
    @Modifying
    @Query("update Board b set b.view_count=b.view_count+1 where b.num=:num")
    int updateCount(Integer num);

    Page<Board> findByTitleContaining(String searchKeyword,Pageable pageable);

    @Modifying
    @Transactional
    @Query("update Board b set b.like_count=b.like_count+1 where b.num=:num")
    Integer updateLikePlus(@Param("num") Integer num);

    @Modifying
    @Transactional
    @Query("update Board b set b.like_count=b.like_count-1 where b.num=:num")
    Integer updateLikeMinus(@Param("num") Integer num);


}
