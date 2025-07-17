package org.spring.metaquery.exceptions.workspace;

/**
 * Exception thrown when a workspace with the same name already exists.
 */
public class WorkspaceNotFoundException extends RuntimeException {
    public WorkspaceNotFoundException(String message) {
        super(message);
    }
}
