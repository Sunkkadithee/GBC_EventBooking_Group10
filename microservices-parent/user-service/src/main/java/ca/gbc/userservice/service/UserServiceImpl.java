package ca.gbc.userservice.service;

import ca.gbc.userservice.model.User;
import ca.gbc.userservice.dto.UserRequest;
import ca.gbc.userservice.dto.UserResponse;
import ca.gbc.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        User user = User.builder()
                .name(userRequest.name())
                .email(userRequest.email())
                .role(userRequest.role())
                .userType(User.UserType.valueOf(userRequest.userType().toUpperCase()))
                .build();

        User savedUser = userRepository.save(user);
        return convertToUserResponse(savedUser);
    }

    @Override
    public Optional<UserResponse> getUserById(Long id) {
        return userRepository.findById(id).map(this::convertToUserResponse);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setName(userRequest.name());
        existingUser.setEmail(userRequest.email());
        existingUser.setRole(userRequest.role());
        existingUser.setUserType(User.UserType.valueOf(userRequest.userType().toUpperCase()));

        User updatedUser = userRepository.save(existingUser);
        return convertToUserResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<UserResponse> getUserByEmail(String email) {
        return userRepository.findByEmail(email).map(this::convertToUserResponse);
    }

    private UserResponse convertToUserResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getUserType());
    }
}
