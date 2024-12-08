package pe.edu.tecsup.learnai.services.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pe.edu.tecsup.learnai.entity.User;
import pe.edu.tecsup.learnai.services.UserService;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        // Verifica que el proveedor de OAuth2 sea Google
        if ("google".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
            DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = principal.getAttributes();

            // Extrae los atributos necesarios de la cuenta de Google
            String email = (String) attributes.getOrDefault("email", "");
            String name = (String) attributes.getOrDefault("name", "");
            boolean isVerified = Boolean.parseBoolean((String) attributes.getOrDefault("verified_email", "false"));

            if (email.isEmpty() || name.isEmpty()) {
                // Si falta información esencial, lanza una excepción para manejarla
                throw new IllegalArgumentException("Email or name is missing in OAuth2 attributes");
            }

            // Busca el usuario en la base de datos por el email
            Optional<User> existingUserOpt = userService.findByEmail(email);

            if (existingUserOpt.isPresent()) {
                // Si el usuario existe, actualiza los datos
                User existingUser = existingUserOpt.get();
                existingUser.setUsername(name);
                existingUser.setVerified(isVerified); // Actualiza si el correo está verificado
                userService.saveUser(existingUser);
            } else {
                // Si el usuario no existe, crea uno nuevo
                User newUser = new User();
                newUser.setUsername(name);
                newUser.setEmail(email);
                newUser.setPassword(generateRandomPassword());  // Genera una contraseña aleatoria segura
                newUser.setVerificationCode(null);  // Puede ser utilizado para una verificación adicional
                newUser.setVerified(isVerified);
                userService.saveUser(newUser);
            }
        }

        // Redirige a la URL del frontend de Vite
        String redirectUrl = "http://localhost:5173"; // URL de Vite (cambia si es necesario)
        response.sendRedirect(redirectUrl); // Redirige al cliente a Vite

        // No es necesario llamar a setAlwaysUseDefaultTargetUrl(true) ni super.onAuthenticationSuccess aquí, ya que hemos manejado la redirección manualmente.
    }

    private String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            password.append((char) (random.nextInt(26) + 'a')); // Solo caracteres alfabéticos en minúsculas
        }
        return password.toString();
    }
}
