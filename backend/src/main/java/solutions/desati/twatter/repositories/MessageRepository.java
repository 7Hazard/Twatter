package solutions.desati.twatter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.desati.twatter.models.Message;
import solutions.desati.twatter.models.Post;
import solutions.desati.twatter.models.User;

import java.util.List;
import java.util.Set;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByFromAndTo(User from, User to);
}
