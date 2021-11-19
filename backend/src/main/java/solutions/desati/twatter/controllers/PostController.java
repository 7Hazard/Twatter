package solutions.desati.twatter.controllers;

import lombok.Data;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.desati.twatter.services.AuthService;
import solutions.desati.twatter.services.PostService;

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
    public ResponseEntity single(@PathVariable Long id) {
        return postService.get(id)
                .map(post -> new ResponseEntity(post.getView(), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    static @Data class Create {
        private String content;
    }
    @PostMapping
    public ResponseEntity create(@RequestBody Create body, @RequestHeader String Authorization) {
        var token = Authorization.replace("Bearer ", "");
        var user = authService.getUserFromToken(token);
        if(user == null) return new ResponseEntity(HttpStatus.UNAUTHORIZED);

        var post = postService.create(user, body.content);
        return new ResponseEntity(post.getView(), HttpStatus.OK);
    }
}
