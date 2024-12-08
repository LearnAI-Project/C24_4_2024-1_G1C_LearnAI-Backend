package pe.edu.tecsup.learnai.services.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pe.edu.tecsup.learnai.entity.User;
import pe.edu.tecsup.learnai.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.UUID;

@Service // Marca esta clase como un servicio en el contexto de Spring
public class EmailService {

    private final JavaMailSender mailSender; // Para enviar correos electrónicos
    private final UserRepository userRepository; // Para interactuar con la base de datos de usuarios

    // Constructor para inyección de dependencias
    public EmailService(JavaMailSender mailSender, UserRepository userRepository) {
        this.mailSender = mailSender;
        this.userRepository = userRepository;
    }

    public void sendWelcomeEmail(String to, String username) {
        String subject = "Welcome to LearnAI!";
        User user = userRepository.findByEmail(to)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + to));
        Integer code = user.getVerificationCode();

        try {
            // Lee la plantilla HTML personalizada
            String content = readTemplate("templates/TemplateEmail.html");
            content = content.replace("{{username}}", username);
            content = content.replace("{{verificationCode}}", code.toString());

            // Crear un correo MIME y configurar su contenido
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            // Enviar el correo
            mailSender.send(message);
        } catch (MessagingException | IOException e) {
            e.printStackTrace(); // Registrar cualquier error en el envío del correo
        }
    }

    private String readTemplate(String templatePath) throws IOException {
        Path path = new ClassPathResource(templatePath).getFile().toPath();
        return Files.readString(path);
    }

    public void sendPasswordResetLink(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email not registered."));
        Integer verificationCode = user.getVerificationCode();
        if (!user.isVerified()) {
            throw new IllegalArgumentException("The email address has not been verified. Please verify your email before requesting a password reset.");
        }

        if (verificationCode == null) {
            verificationCode = 12345678;
            user.setVerificationCode(verificationCode);
            userRepository.save(user);
        }
        String subject = "Password Reset Request - LearnAI";
        try {
            String content = readTemplate("templates/TemplateEmailResetPassword.html");
            content = content.replace("{{username}}", user.getUsername());
            content = content.replace("{{resetCode}}", verificationCode.toString());

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException | IOException e) {
            throw new IllegalStateException("Failed to send email", e);
        }
    }

}
