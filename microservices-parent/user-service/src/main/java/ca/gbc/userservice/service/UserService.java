package ca.gbc.userservice.service;

import ca.gbc.userservice.dto.UserRequest;
import ca.gbc.userservice.model.User;

import java.util.List;

public interface UserService {
    String addUser(UserRequest userRequest);
    void updateUser(Long id, UserRequest userRequest);
    void deleteUser(Long id);
    User getUserById(Long id);
    List<User> getAllUsers();
}
