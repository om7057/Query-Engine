package org.spring.metaquery.dto.auth;

/**
 * LoginResponse is a DTO used to encapsulate the response data for a successful user login.
 * It includes the user's ID, username, email, and profile image URL.
 */
public record LoginResponse(Long id, String username, String email, String profileImageUrl) {
}
