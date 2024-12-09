package pe.edu.tecsup.learnai.paq_domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {

    private Integer id;

    private String username;

    private String email;
}
