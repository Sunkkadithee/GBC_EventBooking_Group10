package ca.gbc.userservice.dto;

public record UserRequest(
        Long id,
        String name,
        String email,
        String role,
        String userType // e.g., "student", "staff", "faculty"
) {}
