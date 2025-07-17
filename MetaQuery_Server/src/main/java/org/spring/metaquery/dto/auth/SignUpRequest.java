package org.spring.metaquery.dto.auth;

/**
 * SignUpRequest is a DTO used for user registration.
 * It contains the user's email, username, and password.
 */
public record SignUpRequest(String email, String username, String password) {
}
