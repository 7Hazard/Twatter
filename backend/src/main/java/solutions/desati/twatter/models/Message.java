package solutions.desati.twatter.models;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@ToString
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Long id;

    @Getter
    @Setter
    private String content;

    @Getter
    @ManyToOne
    private User from;

    @Getter
    @ManyToOne
    private User to;

    public Message(User from, User to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;
    }

    @ToString
    @AllArgsConstructor
    public static @Data class View {
        private Long id;
        private String content;
        private User.View from;
        private User.View to;
    }
    public View getView() {
        return new View(
                id,
                content,
                from.getView(),
                to.getView()
        );
    }
    public static List<View> getView(List<Message> messages) {
        return messages.stream().map(Message::getView).toList();
    }
}
