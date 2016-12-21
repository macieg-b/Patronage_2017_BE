package bartlomiejczyk.maciej.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Holms on 18.12.2016.
 */

@Entity
public class Movie {
    @ManyToMany(mappedBy = "movie")
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

    public Movie(String title, String uri) {
        this.title = title;
        this.uri = uri;
    }

    public Movie(String title, String uri, Long id){
        this.title = title;
        this.uri = uri;
        this.id = id;
    }

    @NotNull
    public String title;

    @NotNull
    public String uri;

    public void deleteActor(Long actorId){
        actors.forEach(
                actor -> {
                    if(actor.getId()==actorId)
                        actors.remove(actor);
                }
        );
    }
    Movie(){

    }
}
