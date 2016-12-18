package bartlomiejczyk.maciej.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Holms on 18.12.2016.
 */

@Entity
public class Movie {
    @OneToMany(mappedBy = "movie")
    private Set<Actor> actors = new HashSet<>();

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String uri;

    public Set<Actor> getActors() {
        return actors;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Movie(String title, String uri) {
        this.title = title;
        this.uri = uri;
    }

    Movie(){

    }
}
