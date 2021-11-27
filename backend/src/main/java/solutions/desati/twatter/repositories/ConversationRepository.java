package solutions.desati.twatter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import solutions.desati.twatter.models.Conversation;
import solutions.desati.twatter.models.Message;
import solutions.desati.twatter.models.User;

import java.util.Collection;
import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    List<Conversation> findAllByParticipantsContainingOrderByLastActivity(User participant);
    Conversation findAllByIdAndParticipantsContaining(Long id, User participants);
}
