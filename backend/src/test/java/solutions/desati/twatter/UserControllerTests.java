package solutions.desati.twatter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import solutions.desati.twatter.models.User;
import solutions.desati.twatter.models.UserToken;
import solutions.desati.twatter.repositories.UserRepository;
import solutions.desati.twatter.repositories.UserTokenRepository;

import java.util.List;

import static org.hamcrest.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTests {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserTokenRepository userTokenRepository;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void create() throws Exception {
        var user1 = new JSONObject();
        user1.put("username", "Test");
        user1.put("password", "test123");
        mvc.perform(post("/user")
                        .header("Content-Type", "application/json")
                        .content(user1.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("token", any(Number.class)));
    }

    @Test
    void followers() throws Exception {
        var popularguy = new User();
        popularguy.username = "popularguy";
        popularguy = userRepository.save(popularguy);

        var fan1 = new User();
        fan1.username = "fan";
        fan1 = userRepository.save(fan1);
        var token = userTokenRepository.save(new UserToken(fan1));

        var fan2 = userRepository.save(new User("fan2"));

        popularguy.getFollowers().addAll(List.of(fan1, fan2));
        popularguy = userRepository.save(popularguy);

        var expected = objectMapper.writeValueAsString(List.of(
                fan1.getView(),
                fan2.getView()
        ));
        mvc.perform(get("/user/"+popularguy.username+"/followers")
                        .header("Authorization", "Bearer " + token.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

//    @Test
//    void posts() throws Exception {
//        var user1 = userRepository.save(new User("postsuser"));
//
//        var post1 = postRepository.save(new Post(user1, "Hello there"));
//        var post2 = postRepository.save(new Post(user1, "Hello again"));
//
//        var expected = objectMapper.writeValueAsString(List.of(
//                post1.getView(),
//                post2.getView()
//        ));
//        mvc.perform(get("/user/"+user1.username+"/posts"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(expected));
//    }

//    @Test
//    void feed() throws Exception {
//        var popularguy = userRepository.save(new User("popularguy2"));
//
//        var post1 = postRepository.save(new Post(popularguy, "Hello there"));
//        var post2 = postRepository.save(new Post(popularguy, "Hello again"));
//
//        var fan = userRepository.save(new User("fan3"));
//        var token = userTokenRepository.save(new UserToken(fan));
//
//        popularguy.getFollowers().addAll(List.of(fan));
//        popularguy = userRepository.save(popularguy);
//
//        var expected = objectMapper.writeValueAsString(List.of(
//                post1.getView(),
//                post2.getView()
//        ));
//        mvc.perform(get("/feed")
//                        .header("Authorization", "Bearer " + token.getId()))
//                .andExpect(status().isOk())
//                .andExpect(content().json(expected));
//    }

    @Test
    void search() throws Exception {
        var john = userRepository.save(new User("John"));
        var doe = userRepository.save(new User("Doe"));

        var expected = objectMapper.writeValueAsString(List.of(
                john.getView(),
                doe.getView()
        ));
        mvc.perform(get("/search/users/o"))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }
}
