package solutions.desati.twatter.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Long id;

    @Getter
    @Setter
    String name;

    /**
     * Owning side
     */
    @ManyToMany
    List<User> participants = new LinkedList<>();

    @OneToMany(mappedBy = "conversation")
    List<Message> messages;

    @Getter
    @Setter
    LocalDateTime lastActivity;

    public Conversation(User from, User to) {
        participants.add(from);
        participants.add(to);
    }

    @ToString
    @AllArgsConstructor
    public static @Data class View {
        Long id;
        String name;
        String lastActivity;
        List<User.View> participants;
    }
    public View getView() {
        return new View(
                id,
                name,
                lastActivity.toString(),
                User.getView(participants)
        );
    }
    public static List<View> getView(List<Conversation> list) {
        return list.stream().map(Conversation::getView).toList();
    }
}
