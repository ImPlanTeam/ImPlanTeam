package What2Do.repository;

import What2Do.domain.Bcomment;
import What2Do.domain.Board;
import What2Do.domain.Comment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface BcommentRepository extends JpaRepository<Bcomment,Integer> {

    Bcomment save(Bcomment bcomment);

    List<Bcomment>  findAllByBoard_num(Integer board_num);

    Bcomment findByNo(Integer no);

    @Modifying
    @Transactional
    @Query("update Bcomment c set c.content= :content where c.no=:no")
    void updateContent(@Param("no") Integer no, @Param("content") String content);

}
