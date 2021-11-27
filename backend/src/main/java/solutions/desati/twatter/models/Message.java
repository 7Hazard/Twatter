package solutions.desati.twatter.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Long id;

    @Getter
    @ManyToOne
    Conversation conversation;

    @Getter
    @ManyToOne
    User from;

    @Getter
    @Setter
    private String content;

    @Getter
    private LocalDateTime time;

    public Message(Conversation conversation, User from, String content) {
        this.conversation = conversation;
        this.from = from;
        this.content = content;
        this.time = LocalDateTime.now();
    }

    @ToString
    @AllArgsConstructor
    public static @Data class View {
        private Long id;
        private Long conversationId;
        private User.View from;
        private String content;
        private String time;
    }
    public View getView() {
        return new View(
                id,
                conversation.getId(),
                from.getView(),
                content,
                time.toString()
        );
    }
    public static List<View> getView(List<Message> messages) {
        return messages.stream().map(Message::getView).toList();
    }
}
