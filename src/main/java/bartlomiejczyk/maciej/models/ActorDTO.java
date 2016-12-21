package bartlomiejczyk.maciej.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * Created by Holms on 20.12.2016.
 */


@Entity
public class ActorDTO {

    @Id
    @GeneratedValue
    private Long id;

    private Long movieId;

    @NotNull
    private String name;




    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getMovieId() {
        return movieId;
    }
}
