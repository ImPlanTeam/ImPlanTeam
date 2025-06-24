package What2Do.repository;


import What2Do.domain.Answer;
import What2Do.domain.Ask;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
        Answer findByAsk(Ask ask);

        Answer findByNo(Integer no);

        @Modifying
        @Transactional
        @Query("update Answer c set c.content= :content where c.no=:no")
        void updateAnswer(@Param("no") Integer no,@Param("content") String content);

}