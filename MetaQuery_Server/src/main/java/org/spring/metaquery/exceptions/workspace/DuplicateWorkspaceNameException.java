package org.spring.metaquery.exceptions.workspace;

/**
 * Exception thrown when a workspace with the same name already exists.
 */
public class DuplicateWorkspaceNameException extends RuntimeException {
    public DuplicateWorkspaceNameException(String message) {
        super(message);
    }
}
