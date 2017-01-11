package bartlomiejczyk.maciej.services.impl;

import bartlomiejczyk.maciej.domain.User;
import bartlomiejczyk.maciej.repositories.UserRepository;
import bartlomiejczyk.maciej.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User save(User userArg) {
        return userRepository.save(new User(userArg.getName()));
    }
}
