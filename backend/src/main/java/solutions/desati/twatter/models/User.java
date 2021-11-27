package solutions.desati.twatter.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "_user")
//@ToString
@RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private long id;

    @Column(unique = true)
    public String username;

    public String password;

    @Column(unique = true)
    public Long githubId;

    @Getter
    @OneToMany(mappedBy = "user")
    private Set<UserToken> tokens = new HashSet<>();

    @Getter
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "_user_followers",
            joinColumns = @JoinColumn(name = "followed"),
            inverseJoinColumns = @JoinColumn(name = "follower"))
    private Set<User> followers = new HashSet<>();

    @Getter
    @ManyToMany(mappedBy = "followers", cascade = CascadeType.REMOVE)
    private Set<User> following = new HashSet<>();

    @Getter
    @OneToMany(mappedBy = "author")
    private Set<Post> posts = new HashSet<>();

    @ManyToMany(mappedBy = "participants")
    private List<Conversation> conversations;

    public User(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o != null) {
            Hibernate.getClass(this);
            Hibernate.getClass(o);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @ToString
    @AllArgsConstructor
    public static @Data class View {
        private Long id;
        private String username;
        private Long githubId;
    }
    public View getView() {
        return new View(
                id,
                username,
                githubId
        );
    }
    public static List<View> getView(List<User> posts) {
        return posts.stream().map(User::getView).toList();
    }
    public static List<View> getView(Set<User> posts) {
        return posts.stream().map(User::getView).toList();
    }
}
