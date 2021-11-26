package solutions.desati.twatter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.desati.twatter.models.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByGithubId(long id);
    List<User> findByUsernameContainsIgnoreCaseOrNameContainsIgnoreCase(String username, String name);
}
