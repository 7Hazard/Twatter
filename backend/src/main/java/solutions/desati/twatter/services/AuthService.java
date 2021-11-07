package solutions.desati.twatter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import solutions.desati.twatter.repositories.UserRepository;

import java.util.List;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    @Value("${twatter.auth.github.client_id}")
    private String githubClientId;

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
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        var map = new LinkedMultiValueMap<String, String>();
        map.add("client_id", githubClientId);
        map.add("client_secret", System.getenv("TWATTER_GITHUB_CLIENT_SECRET"));
        map.add("code", code);

        var request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        var githubResponse = new RestTemplate().postForEntity("https://github.com/login/oauth/access_token", request, String.class);
        var accessToken = new JSONObject(githubResponse.getBody()).getString("access_token");

        // TODO get github user info
        // TODO check if github user is already registered, if not then register
        // TODO generate session token for user, store in db

        return accessToken;
    }
}
