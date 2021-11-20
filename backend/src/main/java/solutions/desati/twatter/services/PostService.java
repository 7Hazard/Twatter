package solutions.desati.twatter.services;

import org.springframework.stereotype.Service;
import solutions.desati.twatter.models.Post;
import solutions.desati.twatter.models.User;
import solutions.desati.twatter.repositories.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Optional<Post> get(Long id) {
        var post = postRepository.findById(id);
        return post;
    }

    public List<Post> getBulk(List<Long> ids) {
        return postRepository.findAllById(ids);
    }

    public Post create(User author, String content) {
        var post = new Post();
        post.author = author;
        post.content = content;
        postRepository.save(post);
        return post;
    }

    public List<Post> getFromUser(Long id) {
        return postRepository.findByAuthor_Id(id);
    }
}
