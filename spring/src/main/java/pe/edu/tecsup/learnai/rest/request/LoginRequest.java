package pe.edu.tecsup.learnai.rest.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    public LoginRequest(String mail, String password123) {
        this.email = mail;
        this.password = password123;
    }
}