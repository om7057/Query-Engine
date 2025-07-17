package org.spring.metaquery.dto.user;

import java.time.LocalDateTime;

/**
 * UpdateUserPasswordResponse is a data transfer object used to encapsulate the response
 * for updating a user's password.
 */
public record UpdateUserPasswordResponse(LocalDateTime updatedAt) {
}
