package What2Do.repository;


import What2Do.domain.Board;
import What2Do.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.util.Assert;

import java.io.Writer;
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

    // 자유게시판의 공지사항 최신3개만 조회
    List<Board> findTop3ByAreaOrderByIndateDesc(String area);

    @Modifying
    @Transactional
    @Query("update Board b set b.like_count=b.like_count-1 where b.num=:num")
    Integer updateLikeMinus(@Param("num") Integer num);

    List<Board> findByWriter(String userId);

    UserRepository userRepository = null;

    long deleteByWriter(String id); // 삭제된 개수 반환

    boolean existsByWriter(String id);

    @Query("SELECT b FROM Board b ORDER BY CASE WHEN b.area = '공지사항' THEN 0 ELSE 1 END, b.num DESC")
    Page<Board> findAllOrderByNoticeFirst(Pageable pageable);

}
