package solutions.desati.twatter.services;

import org.springframework.stereotype.Service;
import solutions.desati.twatter.controllers.UserController;
import solutions.desati.twatter.models.Post;
import solutions.desati.twatter.models.User;
import solutions.desati.twatter.models.UserToken;
import solutions.desati.twatter.repositories.PostRepository;
import solutions.desati.twatter.repositories.UserRepository;
import solutions.desati.twatter.repositories.UserTokenRepository;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    final UserTokenRepository userTokenRepository;
    final PostRepository postRepository;
    final UserRepository userRepository;
    public UserService(UserRepository userRepository, PostRepository postRepository, UserTokenRepository userTokenRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.userTokenRepository = userTokenRepository;
    }

    public void completeDetails(User user, UserController.Details details) {
        user.username = details.getUsername();
        user.name = details.getName();
        userRepository.save(user);
    }

    public User getFromId(Long id) {
        return userRepository.getById(id);
    }

    public List<Post> getFeed(User user) {
        return postRepository.findByAuthor_FollowersIsIn(Set.of(user));
    }

    public void toggleFollow(User follower, User following) {
        var removed = following.getFollowers().removeIf(user -> user.getId() == follower.getId());
        if(!removed)
            following.getFollowers().add(follower);
        userRepository.save(following);
    }

    /**
     * For debugging
     * Create user and return token
     * @return
     */
    public UserToken create(String username) {
        var user = new User();
        user.username = username;
        user = userRepository.save(user);
        var token = new UserToken(user);
        token = userTokenRepository.save(token);
        return token;
    }
}
