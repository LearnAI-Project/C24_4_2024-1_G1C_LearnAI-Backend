package pe.edu.tecsup.learnai.services;

import pe.edu.tecsup.learnai.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getUsers();

    Optional<User> findById(Long id);

    Optional<User> getUserByUsername(String username);

    Optional<User> getUserByEmail(String email);

    boolean hasUserWithUsername(String username);

    boolean hasUserWithEmail(String email);

    User saveUser(User user);

    Optional<User> validEmailAndPassword(String email, String password);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByVerificationCode(Integer code);

    void resetPassword(Integer code, String newPassword);
}
