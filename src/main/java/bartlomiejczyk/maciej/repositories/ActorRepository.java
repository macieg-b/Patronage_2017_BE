package bartlomiejczyk.maciej.repositories;

import bartlomiejczyk.maciej.models.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface ActorRepository extends JpaRepository<Actor, Long>{
    Optional<Actor> findById(Long id);
}