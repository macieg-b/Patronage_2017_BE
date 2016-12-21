package bartlomiejczyk.maciej.domain;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Set;


@Entity
public class MovieDTO {

    @Id
    private Long id;

    @ElementCollection
    private Set<Actor> actors;

    @NotNull
    public String title, uri;

    public MovieDTO(Long id, String title, String uri) {
        this.id = id;
        this.title = title;
        this.uri = uri;
    }

    public MovieDTO(Long id, Set<Actor> actors, String title, String uri) {
        this.id = id;
        this.actors = actors;
        this.title = title;
        this.uri = uri;
    }

    public Long getId() {
        return id;
    }

    public Set<Actor> getActors() {
        return actors;
    }
}
