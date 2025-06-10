package What2Do.repository;


import What2Do.domain.Answer;
import What2Do.domain.Ask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
        Answer findByAsk(Ask ask);

}
