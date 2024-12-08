package pe.edu.tecsup.learnai.rest.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public LoginRequest(String username, String password123) {
        this.username = username;
        this.password = password123;
    }
}