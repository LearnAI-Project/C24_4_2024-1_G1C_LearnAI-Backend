package pe.edu.tecsup.learnai.paq_rest.paq_request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VerificationRequest {
    private Integer code;

    public VerificationRequest() {}

    public VerificationRequest(Integer code) {
        this.code = code;
    }

}