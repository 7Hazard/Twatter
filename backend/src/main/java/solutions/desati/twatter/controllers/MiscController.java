package solutions.desati.twatter.controllers;

import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.desati.twatter.models.Post;
import solutions.desati.twatter.models.User;
import solutions.desati.twatter.services.UserService;

@RestController
@RequestMapping("/")
public class MiscController {
    final UserService userService;
    public MiscController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/search/users/{name}")
    public ResponseEntity search(@PathVariable String name) {
        return new ResponseEntity(
                User.getView(userService.search(name)),
                HttpStatus.OK
        );
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
        userService.completeDetails(user, body.username, body.name);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/feed")
    public ResponseEntity feed(@RequestAttribute User user) {
        return new ResponseEntity(Post.getView(userService.getFeed(user)), HttpStatus.OK);
    }
}
