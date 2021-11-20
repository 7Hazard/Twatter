package solutions.desati.twatter.controllers;

import lombok.Data;
import net.minidev.json.JSONObject;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.desati.twatter.models.Post;
import solutions.desati.twatter.models.User;
import solutions.desati.twatter.services.PostService;
import solutions.desati.twatter.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    final UserService userService;
    final PostService postService;
    public UserController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/status")
    public ResponseEntity status(@RequestAttribute User user){
        var json = new JSONObject();

        { // Missing fields
            var missing = new JSONArray();
            if(user.username == null) missing.put("username");
            if(user.name == null) missing.put("name");
            json.put("missing", missing);
        }

        return new ResponseEntity(json.toString(), HttpStatus.OK);
    }

    public static @Data class Details { private String username, name; }
    @PostMapping("/details")
    public ResponseEntity details(@RequestAttribute User user, @RequestBody Details body) {
        userService.completeDetails(user, body);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity posts(@PathVariable Long id) {
        return new ResponseEntity(Post.viewList(postService.getFromUser(id)), HttpStatus.OK);
    }
}
