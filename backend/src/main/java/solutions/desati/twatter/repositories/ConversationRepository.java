package solutions.desati.twatter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import solutions.desati.twatter.models.Conversation;
import solutions.desati.twatter.models.Message;
import solutions.desati.twatter.models.User;

import java.util.Collection;
import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    List<Conversation> findAllByParticipantsContainingOrderByLastActivityDesc(User participant);
    Conversation findAllByIdAndParticipantsContaining(Long id, User participants);

    @Query(value = "SELECT u FROM Conversation u LEFT JOIN u.participants sk WHERE sk IN :users"
            + " GROUP BY u HAVING COUNT(sk) = :usersSize")
    Conversation findByParticipants(@Param("users") List<User> users,
                            @Param("usersSize") long usersSize);


}
