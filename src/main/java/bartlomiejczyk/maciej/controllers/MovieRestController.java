package bartlomiejczyk.maciej.controllers;

import bartlomiejczyk.maciej.domain.BorrowView;
import bartlomiejczyk.maciej.domain.Movie;
import bartlomiejczyk.maciej.domain.MovieView;
import bartlomiejczyk.maciej.exceptions.MovieCategoryDoesntExist;
import bartlomiejczyk.maciej.exceptions.MovieCountLimitException;
import bartlomiejczyk.maciej.exceptions.MovieNotFoundException;
import bartlomiejczyk.maciej.exceptions.UserNotFoundException;
import bartlomiejczyk.maciej.repositories.MovieRepository;
import bartlomiejczyk.maciej.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/movies")
class MovieRestController {
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    @Autowired
    MovieRestController(MovieRepository movieRepository, UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    Collection<Movie> readMovies() {
        return movieRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{movieId}")
    Movie readMovie(@PathVariable Long movieId) {
        validateMovie(movieId);
        return movieRepository.findOne(movieId);
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<Movie> createMovie(@RequestBody Movie movieArg) throws URISyntaxException {
        validateMovieCategory(movieArg);
        Movie newMovie = movieRepository.save(new Movie(movieArg.title, movieArg.getCategory()));
        return ResponseEntity.created(new URI("/movies/" + newMovie.getId()))
                .header("Movie has been created", HttpStatus.CREATED.toString())
                .body(newMovie);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{movieId}")
    ResponseEntity<Movie> updateMovie(@RequestBody Movie movieArg, @PathVariable Long movieId) throws URISyntaxException {
        if (!movieRepository.findById(movieId).isPresent()) {
            createMovie(movieArg);
        }
        Movie updatedMovie = movieRepository.save(new Movie(movieArg.title, movieId));
        return ResponseEntity.ok()
                .header("Movie with id: " + movieId + " has been updated", HttpStatus.ACCEPTED.toString())
                .body(updatedMovie);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "{movieId}")
    ResponseEntity<Movie> removeMovie(@PathVariable Long movieId) {
        validateMovie(movieId);
        movieRepository.findById(movieId).get().getActors().forEach(
                actor -> actor.unmapMovie(movieId)
        );
        movieRepository.delete(movieId);
        return ResponseEntity.ok()
                .header("Movie with id: " + movieId + " has been deleted", HttpStatus.ACCEPTED.toString())
                .body(null);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/borrow")
    ResponseEntity<BorrowView> borrowMovies(@RequestBody BorrowView borrowView) throws URISyntaxException {
        Movie movie;
        validateUser(borrowView.getUserId());
        Collection<MovieView> borrowedMovieView = new ArrayList<>();
        Collection<Movie> borrowedMovie = new ArrayList<>();
        validateMovieLimit(borrowView.getUserId(), borrowView.getMovies().size());
        for (MovieView movieItem : borrowView.getMovies()) {
            validateMovie(movieItem.getId());
            movie = movieRepository.findById(movieItem.getId()).get();
            borrowedMovie.add(movie);
            if (movie.isAvailable()) {
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
        Double cost;
        newCount = Collections.frequency(categories, "new");
        bestCount = Collections.frequency(categories, "best");
        othersCount = Collections.frequency(categories, "others");
        cost = Integer.valueOf(20 * newCount + 15 * bestCount + 10 * othersCount).doubleValue();
        if (categories.size() == 4 && othersCount >= 1) {
            cost -= 10;
        }
        if (newCount >= 2) {
            cost *= 0.75;
        }

        return ResponseEntity.created(new URI("/movies/borrow"))
                .header("Movies has been borrowed", HttpStatus.CREATED.toString())
                .body(new BorrowView(borrowView.getUserId(), cost, borrowedMovieView));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/return")
    ResponseEntity<List<Movie>> returnMovies(@RequestBody BorrowView borrowView) {
        List<Movie> retunedMovies = new ArrayList<>();
        validateUser(borrowView.getUserId());
        List<Long> usersMoviesIds = new ArrayList<>();
        userRepository.findById(borrowView.getUserId()).get().getBorrowedMovies().forEach(
                movie -> usersMoviesIds.add(movie.getId())
        );
        borrowView.getMovies().forEach(
                movie -> {
                    validateMovie(movie.getId());
                    if (usersMoviesIds.contains(movie.getId())) {
                        retunedMovies.add(movieRepository.findById(movie.getId()).get());
                        movieRepository.save(movieRepository.findById(movie.getId()).get()).setBorrower(null);
                    }
                }
        );
        return ResponseEntity.ok()
                .header("Movie has been returned", HttpStatus.ACCEPTED.toString())
                .body(retunedMovies);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/return")
    Collection<Movie> readAvailableMovies() {
        return movieRepository
                .findAll()
                .stream()
                .filter(movie -> movie.isAvailable())
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/category/{category}")
    Collection<Movie> readMovieByCategory(@PathVariable String category) {
        return movieRepository
                .findAll()
                .stream()
                .filter(movie -> movie.getCategory().equals(category))
                .collect(Collectors.toList());
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
