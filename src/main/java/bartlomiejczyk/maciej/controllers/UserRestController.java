package bartlomiejczyk.maciej.controllers;

import bartlomiejczyk.maciej.domain.User;
import bartlomiejczyk.maciej.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/users")
public class UserRestController {

    public final UserRepository userRepository;

    @Autowired
    UserRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<User> createUser(@RequestBody User userArg) throws URISyntaxException {
        User newUser = userRepository.save(new User(userArg.getName()));
        return ResponseEntity.created(new URI("/users/" + newUser.getId()))
                .header("Movie has been created", HttpStatus.CREATED.toString())
                .body(newUser);
    }

}
