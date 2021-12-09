package solutions.desati.twatter.controllers;

import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.mvc.ProxyExchange;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import solutions.desati.twatter.Helpers;
import solutions.desati.twatter.models.User;
import solutions.desati.twatter.services.ChartService;
import solutions.desati.twatter.services.MessageService;
import solutions.desati.twatter.services.PostService;
import solutions.desati.twatter.services.UserService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    final ChartService chartService;
    final MessageService messageService;
    final UserService userService;
    final PostService postService;
    public UserController(PostService postService, UserService userService, MessageService messageService, ChartService chartService) {
        this.postService = postService;
        this.userService = userService;
        this.messageService = messageService;
        this.chartService = chartService;
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

    @GetMapping("/{username}")
    public ResponseEntity get(@PathVariable String username) {
        var user = userService.get(username);
        if(user == null) return new ResponseEntity(HttpStatus.NOT_FOUND);
        return new ResponseEntity(user.getView(), HttpStatus.OK);
    }

    @GetMapping("/{username}/posts")
    public ResponseEntity posts(@PathVariable String username, ProxyExchange<String> proxy) {

        var user = userService.get(username);
        if(user == null) return ResponseEntity.notFound().build();

        var posts = postService.get(List.of(user.getId()));

        return Helpers.jsonResponse(posts);
    }

    @GetMapping("/{username}/followers")
    public ResponseEntity followers(@PathVariable String username) {
        var user = userService.get(username);
        if(user == null) return new ResponseEntity(HttpStatus.NOT_FOUND);
        var followers = user.getFollowers();
        var views = User.getView(followers);
        return new ResponseEntity(views, HttpStatus.OK);
    }

    @PostMapping("/{username}/followers")
    public ResponseEntity toggleFollow(@RequestAttribute User user, @PathVariable String username) {
        var otherUser = userService.get(username);
        if(otherUser == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        userService.toggleFollow(user, otherUser);
        return new ResponseEntity(HttpStatus.OK);
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
