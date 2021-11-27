package solutions.desati.twatter.controllers;

import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.desati.twatter.models.Message;
import solutions.desati.twatter.models.Post;
import solutions.desati.twatter.models.User;
import solutions.desati.twatter.services.MessageService;
import solutions.desati.twatter.services.PostService;
import solutions.desati.twatter.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    final MessageService messageService;
    final UserService userService;
    final PostService postService;
    public UserController(PostService postService, UserService userService, MessageService messageService) {
        this.postService = postService;
        this.userService = userService;
        this.messageService = messageService;
    }

    @Data
    static class Create{ private String username, password; }
    /**
     * Debug endpoint
     * @return
     */
    @PostMapping
    public ResponseEntity create(@RequestBody Create body){
        if(userService.get(body.username) != null) return new ResponseEntity(HttpStatus.CONFLICT);
        var token = userService.create(body.username, body.password);
        var json = new JSONObject();
        json.put("token", token.getId());
        return new ResponseEntity(json.toString(), HttpStatus.OK);
    }

    @GetMapping("/{username}/posts")
    public ResponseEntity posts(@PathVariable String username) {
        return new ResponseEntity(Post.getView(postService.getFromUser(username)), HttpStatus.OK);
    }

    @PostMapping("/{username}/followers")
    public ResponseEntity unfollow(@RequestAttribute User user, @PathVariable String username) {
        var otherUser = userService.get(username);
        if(otherUser == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        userService.toggleFollow(user, otherUser);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{username}/followers")
    public ResponseEntity followers(@PathVariable String username) {
        var user = userService.get(username);
        if(user == null) return new ResponseEntity(HttpStatus.NOT_FOUND);
        var followers = user.getFollowers();
        var views = User.getView(followers);
        return new ResponseEntity(views, HttpStatus.OK);
    }

    static @Data class SendMessage { private String content; }
    /**
     * Finds or creates a conversation between user and other user
     * @param user
     * @param username
     * @param body
     * @return
     */
    @PostMapping("/{username}/message")
    public ResponseEntity sendMessage(@RequestAttribute User user, @PathVariable String username, @RequestBody SendMessage body) {
        var to = userService.get(username);
        if(to == null) return new ResponseEntity("invalid recipient", HttpStatus.NOT_FOUND);
        return new ResponseEntity(messageService.send(user, to, body.content).getView(), HttpStatus.OK);
    }
}
