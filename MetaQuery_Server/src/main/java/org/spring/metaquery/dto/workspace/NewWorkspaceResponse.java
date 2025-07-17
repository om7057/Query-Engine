package org.spring.metaquery.dto.workspace;

import org.spring.metaquery.entities.Workspace;

import java.time.LocalDateTime;

/**
 * NewWorkspaceResponse is a data transfer object that represents the response after creating a new workspace.
 */
public record NewWorkspaceResponse(Long workspaceId, String workspaceName, String workspaceDescription,
                                   String initialPrompt, LocalDateTime createdAt) {
    public static NewWorkspaceResponse formEntity(Workspace workspace) {
        return new NewWorkspaceResponse(
                workspace.getId(),
                workspace.getWorkspaceName(),
                workspace.getWorkspaceDescription(),
                workspace.getInitialPrompt(),
                workspace.getCreatedAt()
        );
    }
}
