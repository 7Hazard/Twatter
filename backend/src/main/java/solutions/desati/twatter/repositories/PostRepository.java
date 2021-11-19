package solutions.desati.twatter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.desati.twatter.models.Post;
import solutions.desati.twatter.models.User;

public interface PostRepository extends JpaRepository<Post, Long> {

}
