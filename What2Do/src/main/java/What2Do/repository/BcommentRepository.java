package What2Do.repository;

import What2Do.domain.Bcomment;
import What2Do.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BcommentRepository extends JpaRepository<Bcomment,Integer> {

    Bcomment save(Bcomment bcomment);

}
