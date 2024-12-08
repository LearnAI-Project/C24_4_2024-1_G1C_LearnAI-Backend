package pe.edu.tecsup.learnai.rest.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    private Integer id;
    private String username;
    private String email;
    private String token;



    public AuthResponse(Integer id, String username, String email, String token) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.token = token;
    }
}
