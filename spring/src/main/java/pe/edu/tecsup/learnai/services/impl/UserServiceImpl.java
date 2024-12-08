package pe.edu.tecsup.learnai.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import pe.edu.tecsup.learnai.exception.UserNotFoundException;
import pe.edu.tecsup.learnai.repository.UserRepository;
import pe.edu.tecsup.learnai.entity.User;
import pe.edu.tecsup.learnai.services.UserService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {return userRepository.findByEmail(email);}

    @Override
    public boolean hasUserWithUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User saveUser(User user) {
        if (!user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));

        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> validEmailAndPassword(String email, String password) {
        return getUserByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }

    @Override
    public Optional<User> findByVerificationCode(Integer code) {return userRepository.findByVerificationCode(code);}

    @Override
    public void resetPassword(Integer code, String newPassword) {
        User user = userRepository.findByVerificationCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Invalid code"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
