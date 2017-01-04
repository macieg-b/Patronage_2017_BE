package bartlomiejczyk.maciej.controllers;

import bartlomiejczyk.maciej.domain.Actor;
import bartlomiejczyk.maciej.domain.ActorView;
import bartlomiejczyk.maciej.domain.Movie;
import bartlomiejczyk.maciej.exceptions.ActorNotFoundException;
import bartlomiejczyk.maciej.repositories.ActorRepository;
import bartlomiejczyk.maciej.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/actors")
class ActorRestController {
    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;

    @Autowired
    ActorRestController(MovieRepository movieRepository, ActorRepository actorRepository) {
        this.movieRepository = movieRepository;
        this.actorRepository = actorRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    Collection<Actor> readActors() {
        return actorRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{actorId}")
    Actor readActor(@PathVariable Long actorId) {
        validateActor(actorId);
        return actorRepository.findOne(actorId);
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<Actor> createActor(@RequestBody ActorView actorView) throws URISyntaxException {
        Actor newActor = actorRepository.save(new Actor(actorView.getName()));
        return ResponseEntity.created(new URI("/actor/" + newActor.getId()))
                .header("Actor with id: " + newActor.getId() + " has been created", HttpStatus.CREATED.toString())
                .body(newActor);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{actorId}")
    ResponseEntity<Actor> updateActor(@RequestBody ActorView actorView, @PathVariable Long actorId) throws URISyntaxException {
        Actor updatedActor;
        if (actorView.getMovieIds() != null) {
            Set<Movie> movies = new HashSet<>(movieRepository.findAll(actorView.getMovieIds()));
            updatedActor = actorRepository.save(new Actor(movies, actorView.getName(), actorId));
            return ResponseEntity.ok()
                    .header("Actor with id: " + actorId + " has been updated", HttpStatus.ACCEPTED.toString())
                    .body(updatedActor);
        } else {
            if (!actorRepository.findById(actorId).isPresent()) {
                createActor(actorView);
            }
            updatedActor = actorRepository.save(new Actor(actorView.getName(), actorId));
            return ResponseEntity.ok()
                    .header("Actor with id: " + actorId + " has been updated", HttpStatus.ACCEPTED.toString())
                    .body(updatedActor);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{actorId}")
    ResponseEntity<Actor> removeActor(@PathVariable Long actorId) {
        validateActor(actorId);
        actorRepository.delete(actorId);
        return ResponseEntity.ok()
                .header("Actor with id: " + actorId + " has been deleted", HttpStatus.ACCEPTED.toString())
                .body(null);
    }

    private void validateActor(Long actorId) {
        actorRepository.findById(actorId).orElseThrow(
                () -> new ActorNotFoundException(actorId));
    }
}
