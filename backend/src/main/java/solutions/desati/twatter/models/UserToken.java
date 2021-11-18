package solutions.desati.twatter.models;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@ToString
public class UserToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Long id;

    @Getter
    @ManyToOne
    private User user;

    protected UserToken() {}
    public UserToken(User user) {
        this.user = user;
    }
}
