package bartlomiejczyk.maciej.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.Set;


@Entity
public class Actor {

    @JsonIgnore
    @ManyToMany
    private Set<Movie> movies;

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

    public Set<Movie> getMovies() {
        return movies;
    }


    public Actor(String name) {
        this.name = name;
    }

    public Actor(String name, Long id) {
        this.id = id;
        this.name = name;
    }

    public Actor(Set<Movie> movie, String name) {
        this.movies = movie;
        this.name = name;
    }

    public Actor(Set<Movie> movie, String name, Long id) {
        this.movies = movie;
        this.name = name;
        this.id = id;
    }

    public void unmapMovie(Long movieId) {
        for(Movie movie : movies){
            if(movie.getId() == movieId){
                this.movies.remove(movie);
            }
        }
    }


    public Actor() {

    }
}
