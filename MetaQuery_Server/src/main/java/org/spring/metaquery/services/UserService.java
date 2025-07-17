package org.spring.metaquery.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.metaquery.dto.auth.SignUpRequest;
import org.spring.metaquery.entities.User;
import org.spring.metaquery.exceptions.user.DuplicateUsernameException;
import org.spring.metaquery.repositories.UserRepository;
import org.spring.metaquery.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * UserService provides methods to manage user-related operations such as checking if a user exists,
 * saving a new user, and retrieving a user by email.
 * It interacts with the UserRepository to perform database operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public Boolean checkIfUserExists(String email) {
        log.info("Checking if user exists with email: {}", email);
        return userRepository.findByEmail(email).isPresent();
    }

    public Boolean checkIfUserExistsByUsername(String username) {
        log.info("Checking if user exists with username: {}", username);
        return userRepository.findByUsername(username).isPresent();
    }

    public User saveNewUser(SignUpRequest request) {
        User user = User.builder().username(request.username()).email(request.email())
                .passwordHash(passwordEncoder.encode(request.password())).build();
        log.info("Saving new user with email: {}", request.email());
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        log.info("Retrieving user by email: {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public User getUserById(Long userId) {
        log.info("Retrieving user by ID: {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    public User updateUsername(String newUsername, String token) throws DuplicateUsernameException {
        log.info("Updating username to: {}", newUsername);
        if (checkIfUserExistsByUsername(newUsername)) {
            throw new DuplicateUsernameException("Username already exists");
        }

        Long userId = jwtService.extractUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        log.info("Found user with ID: {} for username update", userId);

        user.setUsername(newUsername);
        return userRepository.save(user);
    }

    public User updatePassword(String newPassword, String token) {
        log.info("Updating password for user with token: {}", token);
        Long userId = jwtService.extractUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        log.info("Found user with ID: {} for password update", userId);

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }
}
