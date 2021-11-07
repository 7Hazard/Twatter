package solutions.desati.twatter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.desati.twatter.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByUsernameAndPassword(String username, String password);
}
