package solutions.desati.twatter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solutions.desati.twatter.services.AuthService;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Value("${twatter.auth.redirect}")
    private String redirect;
    @Value("${twatter.auth.github.client_id}")
    private String githubClientId;

    @GetMapping
    public ResponseEntity info() {
        try {
            var json = new JSONObject();
            json.put("redirect", redirect);
            json.put("github.client_id", githubClientId);

            // debug
            json.put("github.client_secret", System.getenv("TWATTER_GITHUB_CLIENT_SECRET"));

            return new ResponseEntity(json.toString(), HttpStatus.OK);
        } catch (JSONException e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/oauth2/github")
    public ResponseEntity loginWithGithub(String code, HttpServletResponse response) {
        try {
            var accessToken = authService.authWithGithub(code);
            response.sendRedirect(redirect + "?token=" + accessToken);
            return new ResponseEntity(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PostMapping("/signin")
//    public ResponseEntity signin(String username, String password) {
//        try {
//            var token = userService.signin(username, password);
//            return new ResponseEntity(token, HttpStatus.ACCEPTED);
//        } catch (Exception e) {
//            return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
//        }
//    }

//    @PostMapping("/signup")
//    public ResponseEntity signup(String username, String password)
//    {
//        try {
//            userService.signup(username, password);
//        } catch (Exception e)
//        {
//            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//
//        return new ResponseEntity(HttpStatus.OK);
//    }
}
