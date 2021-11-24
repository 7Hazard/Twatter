package solutions.desati.twatter.models;

import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@ToString
public class UserToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Long id;

    @Getter
    @ManyToOne(cascade = CascadeType.REMOVE)
    private User user;

    protected UserToken() {}
    public UserToken(User user) {
        this.user = user;
    }
}
