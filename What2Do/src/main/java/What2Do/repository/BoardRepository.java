package What2Do.repository;


import What2Do.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Integer> {
    @Modifying
    @Query("update Board b set b.view_count=b.view_count+1 where b.num=:num")
    int updateCount(Integer num);

    Page<Board> findByAreaContaining(String searchKeyword,Pageable pageable);

    @Modifying
    @Query("update Board b set b.like_count=b.like_count+1 where b.num=:num")
    Integer updateLikePlus(Integer num);

    @Modifying
    @Query("update Board b set b.like_count=b.like_count-1 where b.num=:num")
    int updateLikeMinus(Integer num);


}
