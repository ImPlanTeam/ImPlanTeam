package What2Do.repository;

import What2Do.domain.Comment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    Comment save(Comment comment);

    List<Comment> findAllByTour_id(Long tour_id);

    Comment findByNo(Long no);

    @Modifying
    @Transactional
    @Query("update Comment c set c.content= :content where c.no=:no")
    void updateContent(@Param("no") Long no,@Param("content") String content);

    void deleteByTourId(Long id); // 삭제된 개수 반환
}
