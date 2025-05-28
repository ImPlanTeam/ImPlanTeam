package What2Do.repository;


import What2Do.domain.LikeIt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface LikeRepository extends JpaRepository<LikeIt, Integer> {
    @Modifying
    @Query(value="select l from LikeIt l where l.num.num= :num and l.id.id= :id")
    Integer findByNumAndId(Integer num,String id);

    @Modifying
    @Query(value="delete from LikeIt l where l.num= :num and l.id= :id")
    void deleteByNumAndId(Integer num,String id);

    @Modifying
    @Query(value="insert into LikeIt l(l.num,l.id) values(:num,:id)")
    void insertNumAndID(Integer num,String id);




}