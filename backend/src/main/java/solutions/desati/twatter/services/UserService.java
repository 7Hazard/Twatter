package solutions.desati.twatter.services;

import org.springframework.stereotype.Service;
import solutions.desati.twatter.controllers.UserController;
import solutions.desati.twatter.models.User;
import solutions.desati.twatter.repositories.UserRepository;

@Service
public class UserService {

    final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void completeDetails(User user, UserController.Details details) {
        user.username = details.getUsername();
        user.name = details.getName();
        userRepository.save(user);
    }
}
