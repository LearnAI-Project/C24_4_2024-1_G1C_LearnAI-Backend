package pe.edu.tecsup.learnai.paq_rest.paq_request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RecoverPasswordRequest {
    private Integer code;
    private String newPassword;
}
