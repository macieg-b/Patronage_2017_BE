package bartlomiejczyk.maciej.repositories;

import bartlomiejczyk.maciej.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findById(Long id);
}

