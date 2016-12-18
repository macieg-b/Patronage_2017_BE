package bartlomiejczyk.maciej.repositories;

import bartlomiejczyk.maciej.models.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ActorRepository extends JpaRepository<Actor, Long>{
    Collection<Actor> findByMovieTitle(String title);
}