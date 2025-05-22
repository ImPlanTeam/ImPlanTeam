package com.Implan.What2Do.Repository;

import com.Implan.What2Do.domain.Answer;
import com.Implan.What2Do.domain.Ask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
        Answer findByAsk(Ask ask);

}
