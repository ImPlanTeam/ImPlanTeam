package What2Do.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "LikeIt")
public class LikeIt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "heart_id")
    private Integer no;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User id;

    @ManyToOne
    @JoinColumn(name = "board_num")
    private Board num;

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public User getId() {
        return id;
    }

    public void setId(User id) {
        this.id = id;
    }

    public Board getNum() {
        return num;
    }

    public void setNum(Board num) {
        this.num = num;
    }
}