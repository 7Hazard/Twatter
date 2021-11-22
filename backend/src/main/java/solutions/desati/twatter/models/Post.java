package solutions.desati.twatter.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@ToString
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Long id;

    @Getter
    public String content;

    @Getter
    @ManyToOne
    public User author;

    @ToString
    @AllArgsConstructor
    public static @Data class View {
        private Long id;
        private String content;
        private User.View author;
    }
    public View getView() {
        return new View(
                id,
                content,
                author.getView()
        );
    }
    public static List<View> getView(List<Post> posts) {
        return posts.stream().map(Post::getView).toList();
    }
}
