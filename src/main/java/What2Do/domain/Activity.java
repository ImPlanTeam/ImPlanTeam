package What2Do.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;
    private String weather;      // ex) "맑음;흐림"
    private String mood;         // ex) "우울;지침"
    private String companions;   // ex) "혼자;친구"
    private String tags;         // ex) "실내;휴식"


}
