package org.spring.metaquery.dto.user;

/**
 * UpdateUserPasswordRequest is a data transfer object used to encapsulate the request
 * for updating a user's password.
 */
public record UpdateUserPasswordRequest(String newPassword) {
}
