package bartlomiejczyk.maciej.repositories;

import bartlomiejczyk.maciej.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by Holms on 18.12.2016.
 */
public interface  MovieRepository extends JpaRepository<Movie, Long>{
    Optional<Movie> findById(Long id);
}

