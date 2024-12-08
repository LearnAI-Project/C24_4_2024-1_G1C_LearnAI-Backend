package pe.edu.tecsup.learnai.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pe.edu.tecsup.learnai.entity.User;
import pe.edu.tecsup.learnai.services.UserService;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserService userService;

    @Override
    public void run(String... args) {
        if (!userService.getUsers().isEmpty()) {
            return;
        }
        USERS.forEach(userService::saveUser);
        log.info("Database initialized with user data");
    }

    private static final List<User> USERS = Arrays.asList(
        new User("diego", "diego.becerra@tecsup.edu.pe", "contra123", 99999999, true),
        new User("abel", "abel.santisteban@tecsup.edu.pe", "contra456", 99999998, true),
        new User("jairo", "quispe.coa@tecsup.edu.pe", "contra789", 99999997, true)
    );
}
