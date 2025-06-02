package What2Do.repository;

import What2Do.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findById(String id);

    boolean existsById(String id);

    long deleteById(String id); // 삭제된 개수 반환

    List<User> findByIdContaining(String id);



}
