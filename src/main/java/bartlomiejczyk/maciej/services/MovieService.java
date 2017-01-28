package bartlomiejczyk.maciej.services;

import bartlomiejczyk.maciej.domain.BorrowView;
import bartlomiejczyk.maciej.domain.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieService {
    Page<Movie> readAll(Pageable pageable);

    Movie readOne(Long id);

    Movie save(Movie movie);

    Movie update(Movie movie, Long id);

    void delete(Long id);

    BorrowView borrow(BorrowView borrowView);

    List<Movie> returnMovies (BorrowView borrowView);

    Page<Movie> readByCategory(Pageable pageable, String category);

    Page<Movie> readByUser(Pageable pageable, Long id);

    Page<Movie> readByAvailability(Pageable pageable,Boolean availability);
}
