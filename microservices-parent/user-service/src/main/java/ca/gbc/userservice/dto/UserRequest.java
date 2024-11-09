package ca.gbc.userservice.dto;

import ca.gbc.userservice.model.User.UserType; // Import the UserType enum

public record UserRequest(
        Long id,
        String name,
        String email,
        String role,
        UserType userType // Using the UserType enum
) {}
