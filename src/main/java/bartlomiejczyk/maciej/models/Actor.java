package bartlomiejczyk.maciej.models;

/**
 * Created by Holms on 18.12.2016.
 */
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Actor {

    @JsonIgnore
    @ManyToOne
    private Movie movie;

    @Id
    @GeneratedValue private Long id;
    private String name;

    public Long getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public Movie getMovie(){
        return movie;
    }

    public Actor(Movie movie, String name) {
        this.name = name;
    }

    Actor(){

    }
}
