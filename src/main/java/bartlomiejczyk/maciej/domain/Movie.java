package bartlomiejczyk.maciej.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Movie {
    @NotNull
    public String title;

    @ManyToMany(mappedBy = "movies")
    private Set<Actor> actors = new HashSet<>();

    @ManyToOne
    private User borrower;

    @Id
    @GeneratedValue
    private Long id;

    public Movie(String title) {
        this.title = title;
    }


    public Movie(String title, Long id) {
        this.title = title;
        this.id = id;
    }

    public Movie() {

    }

    public Set<Actor> getActors() {
        return actors;
    }

    public Long getId() {
        return id;
    }
}
