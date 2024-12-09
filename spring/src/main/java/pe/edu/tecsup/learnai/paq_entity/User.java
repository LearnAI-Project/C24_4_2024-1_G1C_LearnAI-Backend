package pe.edu.tecsup.learnai.paq_entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", unique = true, length = 100)
    private String username;
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;
    @Column(name = "password", length = 255)
    private String password;
    @Column(name = "verification_code", length = 8, unique = true)
    private Integer verificationCode;
    @Column(name = "is_verified")
    private boolean isVerified;
    @Column(name = "attempt")
    private Integer attempt;
    @OneToMany(mappedBy = "user")
    private List<Theme> themes;

    public User(String username, String email, String password, Integer verificationCode, boolean isVerified, Integer attempt) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.verificationCode = verificationCode;
        this.isVerified = isVerified;
        this.attempt = attempt;
    }

}
