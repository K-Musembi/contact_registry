package com.backend.backend.user;

import com.backend.backend.user.dto.UserRequest;
import com.backend.backend.user.dto.UserResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for User entity
 * Defines business logic for creating and retrieving data
 * PasswordEncoder for pass word hashing defined in config file
 *
 * @see com.backend.backend.config.SecurityConfig
 * @see com.backend.backend.user.UserRepository
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates a new user
     * @param userRequest object
     * @return UserResponse object
     */
    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.username())) {
            throw new DataIntegrityViolationException("Username already exists");
        }

        User user = new User();
        user.setUsername(userRequest.username());
        user.setPassword(passwordEncoder.encode(userRequest.password()));
        user.setCategory(userRequest.category());

        User createdUser = userRepository.save(user);
        return mapToUserResponse(createdUser);
    }

    /**
     * Updates an existing user
     * @param userRequest object
     * @return UserResponse object
     */
    @Transactional
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Username not found"));

        user.setUsername(userRequest.username());
        user.setPassword(passwordEncoder.encode(userRequest.password()));
        user.setCategory(userRequest.category());

        User updatedUser = userRepository.save(user);
        return mapToUserResponse(updatedUser);
    }

    /**
     * Logs in a user
     * @param username String
     * @param password String
     * @return UserResponse object
     */
    @Transactional
    public UserResponse loginUser(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Username does not exist"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Wrong password");
        }

        return mapToUserResponse(user);
    }

    /**
     * Finds user by Id
     * @param id Long
     * @return UserResponse object
     */
    @Transactional
    public UserResponse findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return mapToUserResponse(user);
    }

    /**
     * Finds user by username
     * @param username String
     * @return UserResponse object
     */
    @Transactional
    public UserResponse findUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return mapToUserResponse(user);
    }

    /**
     * Finds all users
     * @return List of UserResponse objects
     */
    @Transactional
    public List<UserResponse> findAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    /**
     * Deletes a user by id
     * @param id Long
     */
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    protected UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getCategory()
        );
    }
}
