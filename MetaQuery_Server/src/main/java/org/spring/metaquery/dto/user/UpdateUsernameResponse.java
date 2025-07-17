package org.spring.metaquery.dto.user;

import java.time.LocalDateTime;

/**
 * UpdateUsernameResponse is a data transfer object used to encapsulate the response
 * for updating a user's username.
 */
public record UpdateUsernameResponse(String username, LocalDateTime updatedAt) {
}
