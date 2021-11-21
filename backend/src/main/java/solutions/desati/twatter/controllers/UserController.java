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

    /**
     * Debug endpoint
     * @return
     */
    @PostMapping("/create")
    public ResponseEntity create(@RequestParam String username){
        var token = userService.create(username);
        return new ResponseEntity(token.getId(), HttpStatus.OK);
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

    @GetMapping("/feed")
    public ResponseEntity feed(@RequestAttribute User user) {
        return new ResponseEntity(Post.view(userService.getFeed(user)), HttpStatus.OK);
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity posts(@PathVariable Long id) {
        return new ResponseEntity(Post.view(postService.getFromUser(id)), HttpStatus.OK);
    }

    @PostMapping("/{id}/follow")
    public ResponseEntity follow(@RequestAttribute User user, @PathVariable Long id) {
        var otherUser = userService.getFromId(id);
        if(otherUser == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        userService.follow(user, otherUser);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/{id}/unfollow")
    public ResponseEntity unfollow(@RequestAttribute User user, @PathVariable Long id) {
        var otherUser = userService.getFromId(id);
        if(otherUser == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        userService.unfollow(user, otherUser);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{id}/followers")
    public ResponseEntity followers(@PathVariable Long id) {
        var user = userService.getFromId(id);
        var followers = user.getFollowers();
        var views = User.view(followers);
        return new ResponseEntity(views, HttpStatus.OK);
    }
}
