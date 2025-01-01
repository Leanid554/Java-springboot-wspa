package org.jl.projectmanagmentsystem.controller;

import org.jl.projectmanagmentsystem.model.User;
import org.jl.projectmanagmentsystem.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    // Получить всех пользователей
    @GetMapping
    public List<User> getAllUsers() {
        logger.info("Fetching all users");
        List<User> users = userRepository.findAll();
        logger.info("Found {} users", users.size());
        return users;
    }

    // Получить пользователя по ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        logger.info("Fetching user with ID: {}", id);
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("User with ID: {} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    // Создать нового пользователя
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        logger.info("Creating new user: {}", user.getUsername());
        User savedUser = userRepository.save(user);
        logger.info("User created with ID: {}", savedUser.getId());
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    // Обновить пользователя
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        logger.info("Updating user with ID: {}", id);
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUsername(userDetails.getUsername());
            user.setPassword(userDetails.getPassword());
            User updatedUser = userRepository.save(user);
            logger.info("User with ID: {} updated successfully", id);
            return ResponseEntity.ok(updatedUser);
        } else {
            logger.warn("User with ID: {} not found for update", id);
            return ResponseEntity.notFound().build();
        }
    }

    // Удалить пользователя
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logger.info("Deleting user with ID: {}", id);
        userRepository.deleteById(id);
        logger.info("User with ID: {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }
}
