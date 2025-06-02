package What2Do.repository;


import What2Do.domain.Board;
import What2Do.domain.LikeIt;
import What2Do.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


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




}