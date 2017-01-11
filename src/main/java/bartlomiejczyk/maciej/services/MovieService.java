package bartlomiejczyk.maciej.services;

import bartlomiejczyk.maciej.domain.BorrowView;
import bartlomiejczyk.maciej.domain.Movie;

import java.util.List;

public interface MovieService {
    List<Movie> readAll();

    Movie readOne(Long id);

    Movie save(Movie movie);

    Movie update(Movie movie, Long id);

    void delete(Long id);

    BorrowView borrow(BorrowView borrowView);

    List<Movie> returnMovies (BorrowView borrowView);

    List<Movie> readAvailable();

    List<Movie> readByCategory(String category);

    List<Movie> readByUser(Long id);
}
