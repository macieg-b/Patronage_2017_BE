package bartlomiejczyk.maciej.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Movie {
    @ManyToMany(mappedBy = "movies")
    private Set<Actor> actors = new HashSet<>();

    @Id
    @GeneratedValue
    private Long id;

    public Set<Actor> getActors() {
        return actors;
    }

    public Long getId() {
        return id;
    }


    public Movie(String title) {
        this.title = title;
    }

    public Movie(String title, Long id){
        this.title = title;
        this.id = id;
    }

    @NotNull
    public String title;


    public Movie(){

    }
}
