package bartlomiejczyk.maciej.domain;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Set;


@Entity
public class MovieView {

    @NotNull
    public String title, uri;
    @Id
    private Long id;
    @ElementCollection
    private Set<Actor> actors;

    public MovieView(Long id, String title, String uri) {
        this.id = id;
        this.title = title;
        this.uri = uri;
    }

    public MovieView(Long id, Set<Actor> actors, String title, String uri) {
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
