package solutions.desati.twatter.controllers;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.desati.twatter.models.Post;
import solutions.desati.twatter.models.User;
import solutions.desati.twatter.services.AuthService;
import solutions.desati.twatter.services.PostService;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
    final PostService postService;
    final AuthService authService;

    public PostController(PostService postService, AuthService authService) {
        this.postService = postService;
        this.authService = authService;
    }

    @GetMapping("/{id}")
    public ResponseEntity getSingle(@PathVariable Long id) {
        return postService.get(id)
                .map(post -> new ResponseEntity(post.getView(), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    static @Data class GetBulk { private List<Long> ids; }
    @GetMapping
    public ResponseEntity getBulk(@RequestBody GetBulk body) {
        return new ResponseEntity(Post.getView(postService.getBulk(body.ids)), HttpStatus.OK);
    }
}
