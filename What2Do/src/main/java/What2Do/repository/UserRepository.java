package What2Do.repository;

import What2Do.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findById(String id);

    User findByTel(String tel);


    boolean existsById(String id);

    boolean existsByTel(String tel);

    boolean existsByMail(String mail);

    long deleteById(String id); // 삭제된 개수 반환

    List<User> findByIdContaining(String id);

}
