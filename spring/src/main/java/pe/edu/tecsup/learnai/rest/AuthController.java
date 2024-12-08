package pe.edu.tecsup.learnai.rest;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.tecsup.learnai.entity.User;
import pe.edu.tecsup.learnai.exception.DuplicatedUserInfoException;
import pe.edu.tecsup.learnai.exception.InvalidCodeException;
import pe.edu.tecsup.learnai.rest.request.*;
import pe.edu.tecsup.learnai.services.email.EmailService;
import pe.edu.tecsup.learnai.services.UserService;

import java.security.SecureRandom;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController{

    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    public AuthController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        Optional<User> userOptional = userService.validEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (!user.isVerified()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
            return ResponseEntity.ok(new AuthResponse(user.getId(), user.getUsername(), user.getEmail()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody SignUpRequest signUpRequest) {
        // Verificar si el username o email ya están en uso
        if (userService.hasUserWithUsername(signUpRequest.getUsername())) {
            throw new DuplicatedUserInfoException(
                    String.format("Username %s is already been used", signUpRequest.getUsername())
            );
        }
        if (userService.hasUserWithEmail(signUpRequest.getEmail())) {
            throw new DuplicatedUserInfoException(
                    String.format("Email %s is already been used", signUpRequest.getEmail())
            );
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(signUpRequest.getPassword());
        user.setEmail(signUpRequest.getEmail());
        user.setVerificationCode(generateVerificationCode());
        user.setVerified(false);

        userService.saveUser(user);
        emailService.sendWelcomeEmail(user.getEmail(), user.getUsername());

        return new AuthResponse(user.getId(), user.getUsername(), user.getEmail());
    }

    private int generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        return 10000000 + random.nextInt(90000000); // Genera un número entre 10000000 y 99999999
    }

    @PutMapping("/verify")
    public ResponseEntity<String> verifyAccount(@RequestBody VerificationRequest verificationRequest) {
        if (verificationRequest.getCode() == null || verificationRequest.getCode() == 0) {
            return ResponseEntity.badRequest().body("Verification token is missing or invalid.");
        }

        User user = userService.findByVerificationCode(verificationRequest.getCode())
                .orElseThrow(() -> new InvalidCodeException("Invalid verification code"));
        if (user.isVerified()) {
            return ResponseEntity.badRequest().body("Account is already verified.");
        }
        user.setVerified(true);
        userService.saveUser(user);
        return ResponseEntity.ok("Account verified successfully!");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {

        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCodeException("Invalid email"));
        user.setVerificationCode(generateVerificationCode());
        userService.saveUser(user);
        emailService.sendPasswordResetLink(request.getEmail());
        return ResponseEntity.ok("Password reset link sent to your email!");
    }

    @PutMapping("/recover-password")
    public ResponseEntity<String> recoverPassword(@RequestBody RecoverPasswordRequest request) {
        userService.resetPassword(request.getCode(), request.getNewPassword());
        return ResponseEntity.ok("Password reset successfully!");
    }
}