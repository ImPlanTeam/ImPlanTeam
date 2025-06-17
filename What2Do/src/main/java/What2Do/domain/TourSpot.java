package What2Do.domain;

import What2Do.domain.Tour;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class TourSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String restdate;
    private String infocenter;
    private String usetime;
    private String parking;
    private String chkbabycarriage;
    private String chkpet;
    private String contentid;

}