package org.spring.metaquery.dto.workspace;

/**
 * NewWorkspaceRequest is a data transfer object that represents a request to create a new workspace.
 */
public record NewWorkspaceRequest(String workspaceName, String workspaceDescription) {
    public NewWorkspaceRequest {
        if (workspaceName == null || workspaceName.isBlank()) {
            throw new IllegalArgumentException("Workspace name cannot be null or blank");
        }
    }
}
