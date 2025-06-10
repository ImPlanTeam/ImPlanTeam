package What2Do.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Setter
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long no;

    @ManyToOne
    @JoinColumn(name="tour_id")
    private Tour tour;//놀러가자 게시물 id

    private String user; //유저 id

    private String content; //댓글 내용

    @CreationTimestamp
    private String indate; //댓글 단 시간

}
