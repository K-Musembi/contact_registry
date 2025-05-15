package com.backend.backend.user;

import com.backend.backend.user.dto.UserRequest;
import com.backend.backend.user.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller layer for User entity
 * Defines HTTP methods and endpoints for REST API
 * Uses ResponseEntity HTTP object
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Creates a new user
     * @param userRequest object
     * @return ResponseEntity object
     */
    @PostMapping
    public ResponseEntity<UserResponse> createNewUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse responseObject = userService.createUser(userRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseObject);
    }

    /**
     * Updates an existing user
     * @param id Long
     * @param userRequest object
     * @return ResponseEntity object
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateExistingUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest userRequest) {
        UserResponse responseObject = userService.updateUser(id, userRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseObject);
    }

    /**
     * Finds all users
     * @return ResponseEntity object containing list of users
     */
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> responseObject = userService.findAllUsers();

        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    /**
     * Finds user by id
     * @param id Long
     * @return ResponseEntity object containing user
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse responseObject = userService.findUserById(id);

        return ResponseEntity.ok().body(responseObject);
    }

    /**
     * Finds user by username
     * @param username String
     * @return ResponseEntity object
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        UserResponse responseObject = userService.findUserByUsername(username);

        return ResponseEntity.ok().body(responseObject);
    }

    /**
     * Deletes a user
     * @param id Long
     * @return ResponseEntity object
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}
