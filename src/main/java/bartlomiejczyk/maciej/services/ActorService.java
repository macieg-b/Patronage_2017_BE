package bartlomiejczyk.maciej.services;

import bartlomiejczyk.maciej.domain.Actor;
import bartlomiejczyk.maciej.domain.ActorView;

import java.util.List;

public interface ActorService {
    List<Actor> readAll();

    Actor readOne(Long id);

    Actor save(ActorView actorView);

    Actor update(ActorView actorView, Long id);

    void delete(Long id);
}
