package solutions.desati.twatter.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@ToString
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Long id;

    @Getter
    @ManyToOne
    private User author;

    @Getter
    private String content;

    @Getter
    private LocalDateTime time;

    protected Post() {}
    public Post(User author, String content) {
        this.author = author;
        this.content = content;
        this.time = LocalDateTime.now();
    }

    @ToString
    @AllArgsConstructor
    public static @Data class View {
        private Long id;
        private User.View author;
        private String content;
        private String time;
    }
    public View getView() {
        return new View(
                id,
                author.getView(),
                content,
                time.toString()
        );
    }
    public static List<View> getView(List<Post> posts) {
        return posts.stream().map(Post::getView).toList();
    }
}
