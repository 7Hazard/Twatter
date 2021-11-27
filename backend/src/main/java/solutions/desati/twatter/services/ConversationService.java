package solutions.desati.twatter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import solutions.desati.twatter.models.Conversation;
import solutions.desati.twatter.models.Message;
import solutions.desati.twatter.models.User;
import solutions.desati.twatter.repositories.ConversationRepository;
import solutions.desati.twatter.repositories.MessageRepository;
import solutions.desati.twatter.repositories.UserRepository;

import java.util.List;

@Service
public class ConversationService {

    @Autowired
    ConversationRepository conversationRepository;
    @Autowired
    MessageRepository messageRepository;

    /**
     * Retrieves all conversations the user is a part of
     * @param user
     * @return
     */
    public List<Conversation> getAll(User user) {
        return conversationRepository.findAllByParticipantsContainingOrderByLastActivity(user);
    }

    public List<Message> getAllMessages(Conversation conversation) {
        return messageRepository.findByConversationOrderByTimeAsc(conversation);
    }

    /**
     * Gets conversation by id only if user is a part of it
     * @param user
     * @param id
     * @return
     */
    public Conversation getByUserAndId(User user, Long id) {
        return conversationRepository.findAllByIdAndParticipantsContaining(id, user);
    }
}
