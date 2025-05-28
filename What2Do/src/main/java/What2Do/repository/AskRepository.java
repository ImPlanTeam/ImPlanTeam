package What2Do.repository;


import What2Do.domain.Ask;
import What2Do.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AskRepository extends JpaRepository<Ask, Integer> {
    Page<Ask> findByUser(User user, Pageable pageable);
}
