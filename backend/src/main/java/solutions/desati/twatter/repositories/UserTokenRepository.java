package solutions.desati.twatter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.desati.twatter.models.UserToken;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    UserToken findById(long id);
}
