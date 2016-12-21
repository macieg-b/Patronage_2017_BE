package bartlomiejczyk.maciej.models;

/**
 * Created by Holms on 18.12.2016.
 */

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Actor {

    @JsonIgnore
    @ManyToOne
    private Movie movie;

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Movie getMovie() {
        return movie;
    }

    public void unmapMovie() {
        movie = null;
    }

    public Actor(String name) {
        this.name = name;
    }

    public Actor(Movie movie, String name) {
        this.movie = movie;
        this.name = name;
    }

    public Actor(String name, Long id) {
        this.id = id;
        this.name = name;
    }

    public Actor(Movie movie, String name, Long id) {
        this.movie = movie;
        this.name = name;
        this.id = id;
    }


    Actor() {

    }
}
