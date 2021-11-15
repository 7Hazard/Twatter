package solutions.desati.twatter.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/{username}")
public class UserController {
    @GetMapping("/status")
    public ResponseEntity status(
            @PathVariable String username,
            String token
    ){
        // TODO make sure the request comes from the user himself or an authority
        // TODO collect info about missing user info
        return new ResponseEntity(HttpStatus.OK);
    }
}
