package bartlomiejczyk.maciej.controllers;

import bartlomiejczyk.maciej.domain.Movie;
import bartlomiejczyk.maciej.domain.MovieView;
import bartlomiejczyk.maciej.exceptions.MovieNotFoundException;
import bartlomiejczyk.maciej.repositories.ActorRepository;
import bartlomiejczyk.maciej.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/movies")
class MovieRestController {
    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;

    @Autowired
    MovieRestController(MovieRepository movieRepository, ActorRepository actorRepository) {
        this.movieRepository = movieRepository;
        this.actorRepository = actorRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    Collection<MovieView> readMovies() {
        List<MovieView> returnMovies = new ArrayList<>();
        movieRepository.findAll().forEach(
                movie -> returnMovies.add(new MovieView(movie.getId(), movie.getActors(), movie.title, "/movies/" + movie.getId()))
        );
        return returnMovies;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{movieId}")
    Movie readMovie(@PathVariable Long movieId) {
        validateMovie(movieId);
        return movieRepository.findOne(movieId);
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<Movie> createMovie(@RequestBody Movie movieArg) throws URISyntaxException {
        Movie newMovie = movieRepository.save(new Movie(movieArg.title));
        return ResponseEntity.created(new URI("/movie/" + newMovie.getId()))
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

    private void validateMovie(Long movieId) {
        movieRepository.findById(movieId).orElseThrow(
                () -> new MovieNotFoundException(movieId));
    }
}
