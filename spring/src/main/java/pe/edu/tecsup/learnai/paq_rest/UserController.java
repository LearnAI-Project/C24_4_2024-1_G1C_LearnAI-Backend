package pe.edu.tecsup.learnai.paq_rest;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pe.edu.tecsup.learnai.paq_domain.UserDTO;
import pe.edu.tecsup.learnai.paq_entity.ExportedLog;
import pe.edu.tecsup.learnai.paq_entity.Theme;
import pe.edu.tecsup.learnai.paq_entity.User;
import pe.edu.tecsup.learnai.paq_rest.paq_request.ThemeRequest;
import pe.edu.tecsup.learnai.paq_services.ExportedLogService;
import pe.edu.tecsup.learnai.paq_services.ThemeService;
import pe.edu.tecsup.learnai.paq_services.UserService;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final ThemeService themeService;
    private final ExportedLogService exportedLogService;

    @GetMapping("/me")
    public UserDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();  // El email se extrae de la autenticación
            System.out.println("Authenticated username: " + username);  // Log para verificar el email autenticado
            User user = userService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            return new UserDTO(user.getId(), user.getUsername(), user.getEmail());
        } else {
            throw new RuntimeException("User not authenticated");
        }
    }

    @PostMapping("/theme")
    public String createTheme(@RequestBody ThemeRequest themeRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName(); // Obtener el username del usuario autenticado
            User user = userService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Verificar si el usuario tiene intentos restantes
            if (user.getAttempt() <= 0) {
                throw new RuntimeException("No attempts remaining. Please contact support.");
            }

            // Decrementar los intentos del usuario en uno
            user.setAttempt(user.getAttempt() - 1);
            userService.saveUser(user);  // Asegúrate de guardar el usuario después de modificar los intentos

            // Crear y guardar el tema
            Theme theme = new Theme(themeRequest.getName(), user);
            themeService.saveTheme(theme);

            return "Theme '" + themeRequest.getName() + "' created successfully for user: " + username;
        } else {
            throw new RuntimeException("User not authenticated");
        }
    }


    @PostMapping("/export/{themeId}")
    public String exportTheme(@PathVariable Integer themeId) {
        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        String username = authentication.getName(); // Obtener el username del usuario autenticado
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Buscar el tema por ID
        Theme theme = themeService.findThemeById(themeId)
                .orElseThrow(() -> new RuntimeException("Theme not found"));

        // Verificar si el tema pertenece al usuario autenticado
        if (!theme.getUser().equals(user)) {
            throw new RuntimeException("You do not have permission to export this theme");
        }

        // Crear un nuevo registro en ExportedLog
        ExportedLog exportedLog = new ExportedLog(theme);
        exportedLogService.saveExportedLog(exportedLog);

        return "Theme with ID " + themeId + " exported successfully";
    }
}
