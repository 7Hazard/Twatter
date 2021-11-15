package solutions.desati.twatter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import solutions.desati.twatter.models.User;
import solutions.desati.twatter.repositories.UserRepository;

import java.util.List;

@Service
public class AuthService {

    final UserRepository userRepository;
    final UserService userService;

    @Value("${twatter.auth.github.client_id}")
    private String githubClientId;

    public AuthService(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

//    /**
//     * Returns token if username and password pair is valid
//     * @param username
//     * @param password
//     * @return
//     */
//    public String signin(String username, String password) throws Exception {
//        try {
//            var user = userRepository.findByUsernameAndPassword(
//                    username,
//                    BCrypt.withDefaults().hashToString(4, password.toCharArray())
//            );
//            // TODO use JWT
//            return username;
//        } catch (Exception e)
//        {
//            throw new Exception("Bad username or password");
//        }
//    }

//    public User signup(String username, String rawPassword) throws Exception {
//        // Validation
//        if(username.length() < 3 || username.length() > 50)
//            throw new Exception("Username length is not between 3 and 50");
//        if(rawPassword.length() < 3 || rawPassword.length() > 50)
//            throw new Exception("Password length not between 3 and 50");
//        if(userRepository.findByUsername(username) != null)
//            throw new Exception("Username is already taken");
//
//        // Insertion
//        var user = new User();
//        user.username = username;
//        user.password = BCrypt.withDefaults().hashToString(4, rawPassword.toCharArray());
//        user = userRepository.save(user);
//        return user;
//    }

    public String authWithGithub(String code) throws JSONException {
        // Get
        String accessToken;
        {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            var map = new LinkedMultiValueMap<String, String>();
            map.add("client_id", githubClientId);
            map.add("client_secret", System.getenv("TWATTER_GITHUB_CLIENT_SECRET"));
            map.add("code", code);

            var request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            var githubResponse = new RestTemplate().postForEntity("https://github.com/login/oauth/access_token", request, String.class);
            var body = new JSONObject(githubResponse.getBody());
            accessToken = body.getString("access_token");
        }

        // get github user info
        String githubId;
        {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.add("Authorization", "token "+accessToken);
            var request = new HttpEntity(headers);
            var response = new RestTemplate().exchange(
                    "https://api.github.com/user",
                    HttpMethod.GET,
                    request,
                    String.class
            );
            var body = new JSONObject(response.getBody());
            githubId = body.getString("id");
        }

        // get user through github id, if it doesnt exist then create
        var user = userRepository.findByGithubId(githubId);
        if(user == null) {
            user = new User();
            user.githubId = githubId;
            userRepository.save(user);
        }

        // TODO generate session token for user, store in db

        // TODO send generated to user
        return accessToken;
    }

    public String generateToken(User user) {
        // TODO
        return null;
    }

    public boolean validateToken(String token) {
        // TODO validate with UserTokenRepository
        return true;
    }
}
