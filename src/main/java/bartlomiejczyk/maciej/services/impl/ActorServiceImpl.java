package bartlomiejczyk.maciej.services.impl;

import bartlomiejczyk.maciej.domain.Actor;
import bartlomiejczyk.maciej.domain.ActorView;
import bartlomiejczyk.maciej.domain.Movie;
import bartlomiejczyk.maciej.exceptions.ActorNotFoundException;
import bartlomiejczyk.maciej.repositories.ActorRepository;
import bartlomiejczyk.maciej.repositories.MovieRepository;
import bartlomiejczyk.maciej.services.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ActorServiceImpl implements ActorService {

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public List<Actor> readAll() {
        return actorRepository.findAll();
    }

    @Override
    public Actor readOne(Long id) {
        validateActor(id);
        return actorRepository.findOne(id);
    }

    @Override
    public Actor save(ActorView actorView) {
        return actorRepository.save(new Actor(actorView.getName()));
    }

    @Override
    public Actor update(ActorView actorView, Long id) {
        if (actorView.getMovieIds() != null) {
            Set<Movie> movies = new HashSet<>(movieRepository.findAll(actorView.getMovieIds()));
            return actorRepository.save(new Actor(movies, actorView.getName(), id));
        } else {
            if (!actorRepository.findById(id).isPresent()) {
                save(actorView);
            }
            return actorRepository.save(new Actor(actorView.getName(), id));
        }
    }

    @Override
    public void delete(Long id) {
        validateActor(id);
        actorRepository.delete(id);
    }


    private void validateActor(Long actorId) {
        actorRepository.findById(actorId).orElseThrow(
                () -> new ActorNotFoundException(actorId));
    }
}
