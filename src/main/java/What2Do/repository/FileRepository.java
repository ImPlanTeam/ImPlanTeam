package What2Do.repository;



import What2Do.domain.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface FileRepository extends JpaRepository<BoardFile, Integer> {
    List<BoardFile> findByPostnum_Num(Integer num);
}
