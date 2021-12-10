package solutions.desati.twatter.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import solutions.desati.twatter.Env;
import solutions.desati.twatter.models.Conversation;
import solutions.desati.twatter.models.Message;
import solutions.desati.twatter.models.User;
import solutions.desati.twatter.repositories.ConversationRepository;
import solutions.desati.twatter.repositories.MessageRepository;
import solutions.desati.twatter.repositories.UserRepository;

import java.util.List;

@Service
public class MessageService {
    final ConversationRepository conversationRepository;
    final UserRepository userRepository;
    final MessageRepository messageRepository;
    public MessageService(MessageRepository messageRepository, UserRepository userRepository, ConversationRepository conversationRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.conversationRepository = conversationRepository;
    }

    public Message send(User from, User to, String content) {
        var list = List.of(from, to);
        var conv = conversationRepository.findByParticipants(list, list.size());
        if(conv == null) conv = conversationRepository.save(new Conversation(from, to));
        var msg = messageRepository.save(new Message(conv, from, content));
        conv.setLastActivity(msg.getTime());
        conversationRepository.save(conv);
        return msg;
    }

    public Message send(Conversation conv, User from, String content, boolean isImage) {
        var msg = new Message(conv, from, "", isImage);

        if(isImage)
        {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.add("Authorization", "Client-ID "+ Env.imgurClientId);

//            var map = new LinkedMultiValueMap<String, String>();
//            map.add("image", content);
//            map.add("type", "base64");
            var json = new JSONObject();
            json.put("image", content);
            json.put("type", "base64");

            var request = new HttpEntity(json.toString(), headers);
            var response = new RestTemplate().postForEntity("https://api.imgur.com/3/upload", request, String.class);
            var body = new JSONObject(response.getBody());
            var data = body.getJSONObject("data");
            var link = data.getString("link");
            msg.setContent(link);
        } else msg.setContent(content);

        msg = messageRepository.save(msg);
        conv.setLastActivity(msg.getTime());
        conversationRepository.save(conv);
        return msg;
    }
}
