package org.spring.metaquery.dto.auth;

/**
 * LoginRequest is a DTO used to encapsulate the login credentials
 * for a user, including their email and password.
 */
public record LoginRequest(String email, String password) {
}
