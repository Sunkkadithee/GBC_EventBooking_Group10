package ca.gbc.userservice.service;

import ca.gbc.userservice.model.User;
import ca.gbc.userservice.dto.UserRequest;
import ca.gbc.userservice.dto.UserResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // Method to create a new user
    UserResponse createUser(UserRequest userRequest);

    // Method to get user details by ID
    Optional<UserResponse> getUserById(Long id);

    // Method to get all users
    List<UserResponse> getAllUsers();

    // Method to update user details
    UserResponse updateUser(Long id, UserRequest userRequest);

    // Method to delete user by ID
    void deleteUser(Long id);

    // Method to find user by email
    Optional<UserResponse> getUserByEmail(String email);
}
