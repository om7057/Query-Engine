package org.spring.metaquery.exceptions.workspace;

/**
 * Exception thrown when a workspace with the same name already exists.
 */
public class WorkspaceOwnershipException extends RuntimeException {
  public WorkspaceOwnershipException(String message) {
    super(message);
  }
}
