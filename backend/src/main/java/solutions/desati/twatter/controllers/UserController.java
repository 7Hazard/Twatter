package solutions.desati.twatter.controllers;

import net.minidev.json.JSONObject;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;
import solutions.desati.twatter.services.AuthService;

@RestController
@RequestMapping("/user")
public class UserController {

    final AuthService authService;
    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/status")
    public ResponseEntity status(
            @RequestHeader String Authorization
    ){
        // TODO make sure the request comes from the user himself or an authority
        var token = Authorization.replace("Bearer ", "");
        var user = authService.getUserFromToken(token);
        if(user == null) return new ResponseEntity(HttpStatus.UNAUTHORIZED);

        var json = new JSONObject();

        { // Missing fields
            var missing = new JSONArray();
            if(user.username == null) missing.put("username");
            if(user.name == null) missing.put("name");
            json.put("missing", missing);
        }

        return new ResponseEntity(json.toString(), HttpStatus.OK);
    }
}
