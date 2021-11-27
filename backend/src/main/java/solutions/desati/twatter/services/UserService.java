package solutions.desati.twatter.services;

import org.springframework.stereotype.Service;
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

    public void completeDetails(User user, String username) {
        user.username = username;
        userRepository.save(user);
    }

    public User get(Long id) {
        return userRepository.getById(id);
    }

    public User get(String username) {
        return userRepository.findByUsername(username);
    }

    public List<Post> getFeed(User user) {
        return postRepository.findByAuthor_FollowersIsInOrAuthorOrderByTimeDesc(Set.of(user), user);
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
    public UserToken create(String username, String password) {
        var user = new User();
        user.username = username;
        user.password = password;
        user = userRepository.save(user);
        var token = new UserToken(user);
        token = userTokenRepository.save(token);
        return token;
    }

    /**
     * Find all users
     * @param name
     * @return
     */
    public List<User> search(String name) {
        return userRepository.findByUsernameContainsIgnoreCase(name);
    }

    /**
     * Primarly for debugging
     * Creates token if username and password matches
     * @param username
     * @param password
     * @return
     */
    public UserToken login(String username, String password) {
        var user = userRepository.findByUsernameAndPassword(username, password);
        if(user == null) return null;
        return userTokenRepository.save(new UserToken(user));
    }
}
