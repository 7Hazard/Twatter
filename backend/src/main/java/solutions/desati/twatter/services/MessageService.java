package solutions.desati.twatter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import solutions.desati.twatter.models.Conversation;
import solutions.desati.twatter.models.Message;
import solutions.desati.twatter.models.User;
import solutions.desati.twatter.repositories.ConversationRepository;
import solutions.desati.twatter.repositories.MessageRepository;
import solutions.desati.twatter.repositories.UserRepository;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    ConversationRepository conversationRepository;
    final UserRepository userRepository;
    final MessageRepository messageRepository;
    public MessageService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public Message send(User from, User to, String content) {
//        throw new UnsupportedOperationException();
        var list = List.of(from, to);
        var conv = conversationRepository.findByParticipants(list, list.size());
        if(conv == null) conv = conversationRepository.save(new Conversation(from, to));
        var msg = messageRepository.save(new Message(conv, from, content));
        conv.setLastActivity(msg.getTime());
        conversationRepository.save(conv);
        return msg;
    }

    public Message send(Conversation conv, User from, String content) {
        var msg = messageRepository.save(new Message(conv, from, content));
        conv.setLastActivity(msg.getTime());
        conversationRepository.save(conv);
        return msg;
    }
}
