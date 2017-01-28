package bartlomiejczyk.maciej.services.impl;

import bartlomiejczyk.maciej.domain.BorrowView;
import bartlomiejczyk.maciej.domain.Movie;
import bartlomiejczyk.maciej.domain.MovieView;
import bartlomiejczyk.maciej.exceptions.MovieCategoryDoesntExist;
import bartlomiejczyk.maciej.exceptions.MovieCountLimitException;
import bartlomiejczyk.maciej.exceptions.MovieNotFoundException;
import bartlomiejczyk.maciej.exceptions.UserNotFoundException;
import bartlomiejczyk.maciej.repositories.MovieRepository;
import bartlomiejczyk.maciej.repositories.UserRepository;
import bartlomiejczyk.maciej.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class MovieServiceImpl implements MovieService {
    private static final Integer newPrice = 20, bestPrice = 15, othersPrice = 10;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<Movie> readAll(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }


    @Override
    public Movie readOne(Long id) {
        validateMovie(id);
        return movieRepository.findOne(id);
    }

    @Override
    public Movie save(Movie movie) {
        validateMovieCategory(movie);
        return movieRepository.save(new Movie(movie.title, movie.getCategory()));
    }

    @Override
    public Movie update(Movie movie, Long id) {
        if (!movieRepository.findById(id).isPresent()) {
            save(movie);
        }
        return movieRepository.save(new Movie(movie.title, movie.getCategory(), id));
    }

    @Override
    public void delete(Long id) {
        validateMovie(id);
        movieRepository.findById(id).get().getActors().forEach(
                actor -> actor.unmapMovie(id)
        );
        movieRepository.delete(id);
    }

    @Override
    public BorrowView borrow(BorrowView borrowView) {
        Movie movie;
        validateUser(borrowView.getUserId());
        Collection<MovieView> borrowedMovieView = new ArrayList<>();
        Collection<Movie> borrowedMovie = new ArrayList<>();
        validateMovieLimit(borrowView.getUserId(), borrowView.getMovies().size());
        for (MovieView movieItem : borrowView.getMovies()) {
            validateMovie(movieItem.getId());
            movie = movieRepository.findById(movieItem.getId()).get();
            borrowedMovie.add(movie);
            if (movie.getAvailability()) {
                movie.setBorrower(userRepository.findById(borrowView.getUserId()).get());
                movieRepository.save(movie);
                borrowedMovieView.add(new MovieView(movie.getId(), movie.getTitle()));
            }
        }
        /*Count cost*/
        List<String> categories = new ArrayList<>();
        borrowedMovie.forEach(
                movieItem -> {
                    categories.add(movieItem.getCategory());
                });
        Integer newCount, bestCount, othersCount;
        BigDecimal cost;
        newCount = Collections.frequency(categories, "new");
        bestCount = Collections.frequency(categories, "best");
        othersCount = Collections.frequency(categories, "others");
        cost = new BigDecimal(newPrice * newCount + bestPrice * bestCount + othersPrice * othersCount);
        if (categories.size() == 4 && othersCount >= 1) {
            cost.subtract(new BigDecimal(10));
        }
        if (newCount >= 2) {
            cost.multiply(new BigDecimal(0.75));
        }
        return new BorrowView(borrowView.getUserId(), cost.doubleValue(), borrowedMovieView);
    }

    @Override
    public List<Movie> returnMovies(BorrowView borrowView) {
        List<Movie> moviesToReturn = new ArrayList<>();
        List<Long> usersMoviesIds = new ArrayList<>(), requestMoviesIds = new ArrayList<>();
        validateUser(borrowView.getUserId());
        userRepository.findById(borrowView.getUserId()).get().getBorrowedMovies().forEach(
                movie -> usersMoviesIds.add(movie.getId())
        );
        borrowView.getMovies().forEach(movie -> requestMoviesIds.add(movie.getId()));
        usersMoviesIds.retainAll(requestMoviesIds);
        usersMoviesIds.forEach(
                movieId -> {
                    validateMovie(movieId);
                    moviesToReturn.add(movieRepository.findById(movieId).get());
                    movieRepository.save(movieRepository.findById(movieId).get().setBorrower(null));

                }
        );
        return moviesToReturn;
    }

    @Override
    public Page<Movie> readByCategory(Pageable pageable, String category) {
        return movieRepository.findByCategory(pageable, category);
    }

    @Override
    public Page<Movie> readByUser(Pageable pageable, Long id) {
        return movieRepository.findByBorrower_Id(pageable, id);
    }

    @Override
    public Page<Movie> readByAvailability(Pageable pageable, Boolean availability) {
        return movieRepository.findByAvailability(pageable, availability);
    }

    private void validateUser(Long userId) {
        userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(userId));

    }

    private void validateMovieLimit(Long userId, int moviesCount) {
        userRepository.findById(userId)
                .ifPresent(user -> {
                    if (user.getBorrowedMovies().size() + moviesCount > 10) {
                        throw new MovieCountLimitException(userId);
                    }
                });
    }

    private void validateMovie(Long movieId) {
        movieRepository.findById(movieId).orElseThrow(
                () -> new MovieNotFoundException(movieId));
    }

    private void validateMovieCategory(Movie movie) {
        String[] categories = new String[]{"new", "best", "others"};
        if (!Arrays.asList(categories).contains(movie.getCategory())) {
            throw new MovieCategoryDoesntExist(movie.getCategory());
        }
    }
}
