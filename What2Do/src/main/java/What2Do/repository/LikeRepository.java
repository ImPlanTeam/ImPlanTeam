package What2Do.repository;


import What2Do.domain.Board;
import What2Do.domain.LikeIt;
import What2Do.domain.Tour;
import What2Do.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LikeRepository extends JpaRepository<LikeIt, Integer> {

    boolean existsByUserIdAndBoardNum(String userId, Integer boardNum);

    @Modifying
    @Transactional
    @Query(value="delete from LikeIt l where l.board.num= :num and l.user.id= :id")
    void deleteByBoardNumAndUserId(@Param("num") Integer num, @Param("id") String id);

    @Modifying
    @Transactional
    @Query(value="insert into LikeIt l(l.board.num,l.user.id) values(:num,:id)")
    void insertBoardNumAndUserID(@Param("user") User user, @Param("board") Board board);

    @Modifying
    @Transactional
    @Query(value="delete from LikeIt l where l.tour.id= :num and l.user.id= :id")
    void deleteByTourIdAndUserId(@Param("num") Long num, @Param("id") String id);

    boolean existsByUserIdAndTourId(String id, Long num);

    @Modifying
    @Query(value = "select l.board FROM LikeIt l WHERE l.user.id = :userId ORDER BY l.board.num DESC")
    List<Board> findLikedBoardsByUserId(@Param("userId") String userId);

    @Modifying
    @Query(value = "select l.tour FROM LikeIt l WHERE l.user.id = :userId ORDER BY l.tour.id DESC")
    List<Tour> findLikedTourByUserId(@Param("userId") String userId);

    long deleteByUser_id(String id); // 삭제된 개수 반환

    boolean existsByUser_id(String id);

    @Modifying
    @Transactional
    @Query(value="delete from LikeIt l where l.board.num= :num")
    void deleteByBoardId(Integer num);

    void deleteByTourId(Long id); // 삭제된 개수 반환










}