package bartlomiejczyk.maciej.controllers;

import bartlomiejczyk.maciej.exceptions.ActorNotFoundException;
import bartlomiejczyk.maciej.exceptions.MovieNotFoundException;
import bartlomiejczyk.maciej.models.Actor;
import bartlomiejczyk.maciej.models.ActorDTO;
import bartlomiejczyk.maciej.models.Movie;
import bartlomiejczyk.maciej.repositories.ActorRepository;
import bartlomiejczyk.maciej.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

/**
 * Created by Holms on 20.12.2016.
 */
@RestController
@RequestMapping("/actor")
class ActorRestController {
    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;

    @Autowired
    ActorRestController(MovieRepository movieRepository, ActorRepository actorRepository){
        this.movieRepository = movieRepository;
        this.actorRepository = actorRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    Collection<Actor> readActors() {
        return actorRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{actorId}")
    Actor readActor(@PathVariable Long actorId){
        validateActor(actorId);
        return actorRepository.findOne(actorId);
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<Actor> createActor(@RequestBody ActorDTO actorDto) throws URISyntaxException {
        Actor newActor = actorRepository.save(new Actor(actorDto.getName()));
        return ResponseEntity.created(new URI("/actor/" + newActor.getId()))
                .header("Actor with id: " + newActor.getId() + " has been created", HttpStatus.CREATED.toString())
                .body(newActor);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{actorId}")
    ResponseEntity<Actor> updateActor(@RequestBody ActorDTO actorDto, @PathVariable Long actorId) throws URISyntaxException {
        Actor updatedActor;
        if (actorDto.getMovieId() != null) {
            validateMovie(actorDto.getMovieId());
            updatedActor = actorRepository.save(new Actor(movieRepository.getOne(actorDto.getMovieId()), actorDto.getName(), actorId));
            return ResponseEntity.ok()
                    .header("Actor with id: " + actorId + " has been updated", HttpStatus.ACCEPTED.toString())
                    .body(updatedActor);
        } else {
            if (!actorRepository.findById(actorId).isPresent()) {
                createActor(actorDto);
            }
            updatedActor = actorRepository.save(new Actor(actorDto.getName(), actorId));
            return ResponseEntity.ok()
                    .header("Actor with id: " + actorId + " has been updated", HttpStatus.ACCEPTED.toString())
                    .body(updatedActor);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{actorId}")
    ResponseEntity<Actor> removeActor(@PathVariable Long actorId){
        validateActor(actorId);
        actorRepository.delete(actorId);
        return ResponseEntity.ok()
                .header("Actor with id: " + actorId + " has been deleted", HttpStatus.ACCEPTED.toString())
                .body(null);
    }

    private void validateActor(Long actorId){
        actorRepository.findById(actorId).orElseThrow(
                () -> new ActorNotFoundException(actorId));
    }

    private void validateMovie(Long movieId){
        movieRepository.findById(movieId).orElseThrow(
                () -> new MovieNotFoundException(movieId));
    }
}