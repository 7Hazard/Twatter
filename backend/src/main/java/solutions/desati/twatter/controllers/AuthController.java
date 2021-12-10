package solutions.desati.twatter.controllers;

import lombok.Data;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.desati.twatter.Env;
import solutions.desati.twatter.services.AuthService;
import solutions.desati.twatter.services.UserService;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Value("${twatter.auth.redirect}")
    private String redirect;
    @Value("${twatter.auth.github.client_id}")
    private String githubClientId;

    final UserService userService;
    final AuthService authService;
    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity info() {
        var json = new JSONObject();
        json.put("redirect", redirect);
        json.put("github.client_id", githubClientId);

        // debug
        json.put("github.client_secret", Env.githubClientSecret);

        return new ResponseEntity(json.toString(), HttpStatus.OK);
    }

    @GetMapping("/oauth2/github")
    public ResponseEntity loginWithGithub(String code, HttpServletResponse response) throws Exception {
        var accessToken = authService.authWithGithub(code);
        response.sendRedirect(redirect + "?token=" + accessToken);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @Data
    static class Signin { private String username, password; }
    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody Signin body){
        var token = userService.login(body.username, body.password);
        if(token == null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        var json = new JSONObject();
        json.put("token", token.getId());
        return new ResponseEntity(json.toString(), HttpStatus.OK);
    }
}
