package bartlomiejczyk.maciej.controllers;

import bartlomiejczyk.maciej.controllers.util.PaginationUtil;
import bartlomiejczyk.maciej.domain.BorrowView;
import bartlomiejczyk.maciej.domain.Movie;
import bartlomiejczyk.maciej.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


@RestController
@RequestMapping(value = "/movies", produces = {MediaType.APPLICATION_JSON_VALUE})
class MovieRestController {

    @Autowired
    private MovieService service;

    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<List<Movie>> readMovies(Pageable pageable,
                                           @RequestParam(value = "userId", required = false) Long userId,
                                           @RequestParam(value = "category", required = false) String category,
                                           @RequestParam(value = "availability", required = false) Boolean availability
    ) throws URISyntaxException {
        Page<Movie> page = null;
        if (userId == null && category == null && availability == null) {
            page = service.readAll(pageable);
        } else if (userId != null) {
            page = service.readByUser(pageable, userId);
        } else if (category != null) {
            page = service.readByCategory(pageable, category);
        } else if (availability != null) {
            page = service.readByAvailability(pageable, availability);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/movies");
        headers.add(HttpHeaders.CACHE_CONTROL, "max-age=300");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{movieId}")
    Movie readMovie(@PathVariable Long movieId) {
        return service.readOne(movieId);
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<Movie> createMovie(@RequestBody Movie movieArg) throws URISyntaxException {
        Movie newMovie = service.save(movieArg);
        return ResponseEntity.created(new URI("/movies/" + newMovie.getId()))
                .header("Movie has been created", HttpStatus.CREATED.toString())
                .body(newMovie);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{movieId}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<Movie> updateMovie(@RequestBody Movie movieArg, @PathVariable Long movieId) throws URISyntaxException {
        Movie updatedMovie = service.update(movieArg, movieId);
        return ResponseEntity.ok()
                .header("Movie with id: " + movieId + " has been updated", HttpStatus.ACCEPTED.toString())
                .body(updatedMovie);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{movieId}")
    ResponseEntity<Movie> removeMovie(@PathVariable Long movieId) {
        service.delete(movieId);
        return ResponseEntity.ok()
                .header("Movie with id: " + movieId + " has been deleted", HttpStatus.ACCEPTED.toString())
                .body(null);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/borrow", consumes = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<BorrowView> borrowMovies(@RequestBody BorrowView borrowView) throws URISyntaxException {
        BorrowView result = service.borrow(borrowView);
        return ResponseEntity.created(new URI("/movies/borrow"))
                .header("Movies has been borrowed", HttpStatus.CREATED.toString())
                .body(result);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/return", consumes = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<List<Movie>> returnMovies(@RequestBody BorrowView borrowView) {
        List<Movie> result = service.returnMovies(borrowView);
        return ResponseEntity.ok()
                .header("Movie has been returned", HttpStatus.ACCEPTED.toString())
                .body(result);
    }


}
