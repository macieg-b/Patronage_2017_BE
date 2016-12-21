package bartlomiejczyk.maciej.repositories;

import bartlomiejczyk.maciej.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

/**
 * Created by Holms on 18.12.2016.
 */
public interface  MovieRepository extends JpaRepository<Movie, Long>{
    Optional<Movie> findById(Long id);
}

