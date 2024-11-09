package ca.gbc.userservice.dto;

import ca.gbc.userservice.model.User.UserType; // Importing the UserType enum

public record UserResponse(
        Long id,
        String name,
        String email,
        String role,
        UserType userType // Enum for user types
) {}
