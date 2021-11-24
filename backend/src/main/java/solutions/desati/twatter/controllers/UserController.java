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
        return new ResponseEntity(Post.getView(userService.getFeed(user)), HttpStatus.OK);
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity posts(@PathVariable Long id) {
        return new ResponseEntity(Post.getView(postService.getFromUser(id)), HttpStatus.OK);
    }

    static @Data class CreatePost { private String content; }
    /**
     * id path variable is disregarded
     * @param body
     * @param user
     * @return
     */
    @PostMapping("/{id}/posts")
    public ResponseEntity createPost(@RequestBody CreatePost body, @RequestAttribute User user) {
        var post = postService.create(user, body.content);
        return new ResponseEntity(post.getView(), HttpStatus.OK);
    }

    @PostMapping("/{id}/followers")
    public ResponseEntity unfollow(@RequestAttribute User user, @PathVariable Long id) {
        var otherUser = userService.getFromId(id);
        if(otherUser == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        userService.toggleFollow(user, otherUser);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{id}/followers")
    public ResponseEntity followers(@PathVariable Long id) {
        var user = userService.getFromId(id);
        var followers = user.getFollowers();
        var views = User.getView(followers);
        return new ResponseEntity(views, HttpStatus.OK);
    }

    /**
     * id is 'to'
     * authed user is 'from'
     * @param from
     * @return
     */
    @GetMapping("/{id}/messages")
    public ResponseEntity getMessages(@RequestAttribute User from, @PathVariable Long id) {
        var to = userService.getFromId(id);
        if(to == null) return new ResponseEntity("invalid id", HttpStatus.NOT_FOUND);
        return new ResponseEntity(Message.getView(messageService.get(from, to)), HttpStatus.OK);
    }

    static @Data class SendMessage { private String content; }
    /**
     * id is 'to'
     * authed user is 'from'
     * @param body
     * @param from
     * @param id
     * @return
     */
    @PostMapping("{id}/messages")
    public ResponseEntity sendMessage(@RequestBody SendMessage body, @RequestAttribute User from, @PathVariable Long id) {
        var to = userService.getFromId(id);
        if(to == null) return new ResponseEntity("invalid recipient id", HttpStatus.NOT_FOUND);
        return new ResponseEntity(messageService.send(from, to, body.content).getView(), HttpStatus.OK);
    }
}
