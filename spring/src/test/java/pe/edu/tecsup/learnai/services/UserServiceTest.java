package pe.edu.tecsup.learnai.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pe.edu.tecsup.learnai.entity.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class UserServiceTest {

    @Autowired
    private UserService userService;
/*
    @Test
    public void testSaveUser() {

        String username = "diego becerra";
        String email = "diego.becerra@tecsup.com";
        String rawpassword = "contra123456";
        String verificationToken = "54321token12345";
        boolean isVerified = true;

        User user = new User(username, email, rawpassword, verificationToken, isVerified);

        User userCreated = this.userService.saveUser(user);
        String password = userCreated.getPassword();

        log.info("User created: {}", userCreated);
        assertNotNull(userCreated);
        assertEquals(username, userCreated.getUsername());
        assertEquals(email, userCreated.getEmail());
        assertEquals(password, userCreated.getPassword());
        assertEquals(verificationToken, userCreated.getVerificationToken());
        assertEquals(isVerified, userCreated.isVerified());
    }

    @Test
    void testGetUsers() {
        List<User> users = userService.getUsers();

        assertNotNull(users);
        assertEquals(3, users.size());
    }

    @Test
    void testGetUserByUsername() {
        String username = "diego";

        Optional<User> user = userService.getUserByUsername(username);

        assertTrue(user.isPresent());
        assertEquals(username, user.get().getUsername());
    }

    @Test
    void testGetUserByEmail() {
        String email = "diego.becerra@example.com";

        Optional<User> user = userService.getUserByEmail(email);

        assertTrue(user.isPresent());
        assertEquals(email, user.get().getEmail());
    }

    @Test
    void testHasUserWithEmail() {
        String email = "diego.becerra@example.com";

        boolean result = userService.hasUserWithEmail(email);
        assertTrue(result);
    }

    @Test
    void testHasUserWithUsername() {
        String username = "diego";

        boolean result = userService.hasUserWithUsername(username);
        assertTrue(result);
    }

    @Test
    void testValidEmailAndPassword() {
        String email = "diego.becerra@example.com";
        String password = "contra123";

        Optional<User> result = userService.validEmailAndPassword(email, password);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
    }

    @Test
    void testFindByVerificationToken() {
        String token = "token12345";

        Optional<User> result = userService.findByVerificationToken(token);

        assertTrue(result.isPresent());
        assertEquals(token, result.get().getVerificationToken());
    }

    @Test
    void testFindByEmail() {
        String email = "diego.becerra@example.com";

        Optional<User> result = userService.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
    }

    @Test
    void testResetPassword() {
        String username = "diego mauricio";
        String email = "diego.becerra@sdadasd.com";
        String rawpassword = "contra123456789";
        String verificationToken = "54321token1234523132";
        boolean isVerified = true;

        User user = new User(username, email, rawpassword, verificationToken, isVerified);
        User userCreated = this.userService.saveUser(user);

        String token = "54321token1234523132";
        String newPassword = "newPassword123";
        this.userService.resetPassword(token, newPassword);

        String password = userCreated.getPassword();
        assertEquals(password, userCreated.getPassword());
    }*/
}
