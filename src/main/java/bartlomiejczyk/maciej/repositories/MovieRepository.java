package bartlomiejczyk.maciej.repositories;

import bartlomiejczyk.maciej.domain.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findById(Long id);

    Page<Movie> findByBorrower_Id(Pageable pageable, Long borrowerId);

    Page<Movie> findByCategory(Pageable pageable, String category);

    Page<Movie> findByAvailability(Pageable pageable, Boolean availability);
}
