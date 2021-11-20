package solutions.desati.twatter.models;

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

    public static @Data class View {
        private Long id;
        private String content;
        private Long userId;
    }
    public View getView() {
        var view = new View();
        view.id = this.id;
        view.content = this.content;
        view.userId = this.author.getId();
        return view;
    }
    public static List<View> viewList(List<Post> posts) {
        return posts.stream().map(Post::getView).toList();
    }
}
