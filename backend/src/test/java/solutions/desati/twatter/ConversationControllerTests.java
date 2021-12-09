package solutions.desati.twatter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import solutions.desati.twatter.models.Conversation;
import solutions.desati.twatter.models.User;
import solutions.desati.twatter.models.UserToken;
import solutions.desati.twatter.repositories.ConversationRepository;
import solutions.desati.twatter.repositories.UserRepository;
import solutions.desati.twatter.repositories.UserTokenRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ConversationControllerTests {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserTokenRepository userTokenRepository;
    @Autowired
    ConversationRepository conversationRepository;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void directMessage() throws Exception {
        var user1 = userRepository.save(new User("user1"));
        var user1token = userTokenRepository.save(new UserToken(user1));
        var user2 = userRepository.save(new User("user2"));
        var user2token = userTokenRepository.save(new UserToken(user2));

        var message1 = new JSONObject(); message1.put("content", "Hello");
        var response1 = mvc.perform(post("/user/"+user1.username+"/message")
                        .header("Authorization", "Bearer " + user1token.getId())
                        .header("Content-Type", "application/json")
                        .content(message1.toString()))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        System.out.println("\n"+response1.getContentAsString()+"\n");

        // TODO h2 db issue, creates new convo when it should use the same one
        var message2 = new JSONObject(); message2.put("content", "Hello back");
        var response2 = mvc.perform(post("/user/"+user2.username+"/message")
                        .header("Authorization", "Bearer " + user2token.getId())
                        .header("Content-Type", "application/json")
                        .content(message2.toString()))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        System.out.println("\n"+response2.getContentAsString()+"\n");
    }

    @Test
    public void conversation() throws Exception {
        var user1 = userRepository.save(new User("user3"));
        var user1token = userTokenRepository.save(new UserToken(user1));
        var user2 = userRepository.save(new User("user4"));
        var user2token = userTokenRepository.save(new UserToken(user2));

        var conversation = conversationRepository.save(new Conversation(user1, user2));

        var message1 = new JSONObject(); message1.put("content", "Hello");
        var response1 = mvc.perform(post("/conversations/"+conversation.getId()+"/messages")
                        .header("Authorization", "Bearer " + user1token.getId())
                        .header("Content-Type", "application/json")
                        .content(message1.toString()))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        System.out.println("\n"+response1.getContentAsString()+"\n");

        var message2 = new JSONObject(); message2.put("content", "Hello back");
        var response2 = mvc.perform(post("/conversations/"+conversation.getId()+"/messages")
                        .header("Authorization", "Bearer " + user2token.getId())
                        .header("Content-Type", "application/json")
                        .content(message2.toString()))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        System.out.println("\n"+response2.getContentAsString()+"\n");
    }
}
