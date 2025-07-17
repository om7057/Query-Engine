package org.spring.metaquery.exceptions.user;

/**
 * Exception thrown when a user tries to register with a username that already exists.
 */
public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException(String message) {
        super(message);
    }
}
