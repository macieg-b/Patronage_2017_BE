package bartlomiejczyk.maciej.controllers;

import bartlomiejczyk.maciej.models.Movie;
import bartlomiejczyk.maciej.repositories.ActorRepository;
import bartlomiejczyk.maciej.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * Created by Holms on 18.12.2016.
 */
@RestController
@RequestMapping("/movie")
class MovieRestController {
    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;

    @Autowired
    MovieRestController(MovieRepository movieRepository, ActorRepository actorRepository){
        this.movieRepository = movieRepository;
        this.actorRepository = actorRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    Collection<Movie> readMovie(){
        return this.movieRepository.findAll();
    }

}
