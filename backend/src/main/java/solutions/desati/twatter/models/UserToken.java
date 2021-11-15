package solutions.desati.twatter.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;

@Entity
@ToString
@RequiredArgsConstructor
public class UserToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private long id;

    @Getter
    @ManyToOne
    private User user;
}
