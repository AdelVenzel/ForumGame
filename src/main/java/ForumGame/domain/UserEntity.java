package ForumGame.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "my_user")

public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String login;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role roles;
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @JsonIgnore
    @OneToMany(mappedBy = "userEntity")
    private List<Post> posts;
    @JsonIgnore
    @OneToMany(mappedBy = "userEntity")
    private List<Comment> comments;
}

