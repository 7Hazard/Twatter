package solutions.desati.twatter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import solutions.desati.twatter.models.Post;
import solutions.desati.twatter.models.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthor(User author);
    List<Post> findByAuthor_Id(long id);
    List<Post> findByAuthor_Username(String username);
    List<Post> findByAuthor_FollowersIsInOrAuthorIs(Set<User> followers, User self);
    List<Post> findByAuthor_FollowersIsIn(Set<User> followers);

    List<Post> findByAuthor_FollowersIsInOrAuthorOrderByTimeAsc(Collection<User> followers, User author);

}
