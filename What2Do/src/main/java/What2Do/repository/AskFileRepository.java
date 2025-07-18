package What2Do.repository;

import What2Do.domain.AskFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AskFileRepository extends JpaRepository<AskFile, Integer> {
    List<AskFile> findByAsk_no(Integer no);

}
