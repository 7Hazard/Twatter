package solutions.desati.twatter.services;

import org.springframework.stereotype.Service;
import solutions.desati.twatter.models.Message;
import solutions.desati.twatter.models.User;
import solutions.desati.twatter.repositories.MessageRepository;

import java.util.List;

@Service
public class MessageService {
    final MessageRepository messageRepository;
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message send(User from, User to, String content) {
        var msg = new Message(from, to, content);
        msg = messageRepository.save(msg);
        return msg;
    }

    public List<Message> get(User from, User to) {
        return messageRepository.findByFromAndTo(from, to);
    }
}
