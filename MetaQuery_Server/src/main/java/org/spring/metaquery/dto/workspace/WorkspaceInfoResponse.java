package org.spring.metaquery.dto.workspace;

import org.spring.metaquery.entities.Workspace;

import java.time.LocalDateTime;

/**
 * WorkspaceInfoResponse is a data transfer object that represents the information of a workspace.
 */
public record WorkspaceInfoResponse(
        Long workspaceId,
        String workspaceName,
        String workspaceDescription,
        LocalDateTime workspaceCreatedAt
) {
    public static WorkspaceInfoResponse fromEntity(Workspace workspace) {
        return new WorkspaceInfoResponse(
                workspace.getId(),
                workspace.getWorkspaceName(),
                workspace.getWorkspaceDescription(),
                workspace.getCreatedAt()
        );
    }
}
