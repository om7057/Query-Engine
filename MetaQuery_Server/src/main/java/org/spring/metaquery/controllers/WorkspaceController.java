package org.spring.metaquery.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.metaquery.dto.ApiResponse;
import org.spring.metaquery.dto.workspace.NewWorkspaceRequest;
import org.spring.metaquery.dto.workspace.NewWorkspaceResponse;
import org.spring.metaquery.dto.workspace.WorkspaceDescriptionUpdateRequest;
import org.spring.metaquery.dto.workspace.WorkspaceInfoResponse;
import org.spring.metaquery.services.WorkspaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * WorkspaceController handles all workspace-related operations.
 * It provides endpoints to create, retrieve, update, and delete workspaces.
 */
@Slf4j
@RestController
@RequestMapping("/api/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {
    private final WorkspaceService workspaceService;

    /**
     * Retrieves a workspace by its ID.
     *
     * @param workspaceId the ID of the workspace to retrieve
     * @param token       the JWT token for authentication
     * @return a ResponseEntity containing the workspace information
     */
    @GetMapping("/{workspaceId}")
    public ResponseEntity<ApiResponse<WorkspaceInfoResponse>> getWorkspaceById(@PathVariable Long workspaceId,
                                                                               @CookieValue("jwt") String token) {
        log.info("Received request to get workspace with ID: {}", workspaceId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Workspace retrieved",
                workspaceService.getWorkspaceById(workspaceId, token)));
    }

    /**
     * Retrieves all workspaces for the authenticated user.
     *
     * @param token the JWT token for authentication
     * @return a ResponseEntity containing a list of workspaces
     */
    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<WorkspaceInfoResponse>>> getAllWorkspaces(@CookieValue("jwt") String token) {
        log.info("Received request to get all workspaces for user with token: {}", token);
        return ResponseEntity.ok(new ApiResponse<>(true, "Workspaces retrieved",
                workspaceService.getAllWorkspaces(token)));
    }

    /**
     * Creates a new workspace.
     *
     * @param request the request containing workspace details
     * @param token   the JWT token for authentication
     * @return a ResponseEntity containing the newly created workspace information
     */
    @PostMapping("/create-new")
    public ResponseEntity<ApiResponse<NewWorkspaceResponse>> createNewWorkspace(@RequestBody NewWorkspaceRequest request, @CookieValue("jwt") String token) {
        log.info("Received request to create a new workspace: {}", request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Workspace created",
                workspaceService.createNewWorkspace(request, token)));
    }

    /**
     * Updates the description of an existing workspace.
     *
     * @param workspaceId the ID of the workspace to update
     * @param request     the request containing the new description
     * @param token       the JWT token for authentication
     * @return a ResponseEntity containing the updated workspace information
     */
    @PatchMapping("/update-description/{workspaceId}")
    public ResponseEntity<ApiResponse<WorkspaceInfoResponse>> updateWorkspaceDescription(
            @PathVariable Long workspaceId,
            @RequestBody WorkspaceDescriptionUpdateRequest request,
            @CookieValue("jwt") String token) {
        log.info("Received request to update workspace description for ID: {} with new description: {}", workspaceId, request.newDescription());
        return ResponseEntity.ok(new ApiResponse<>(true, "Workspace description updated",
                workspaceService.updateWorkspaceDescription(workspaceId, request.newDescription(), token)));
    }

    /**
     * Deletes a workspace by its ID.
     *
     * @param workspaceId the ID of the workspace to delete
     * @param token       the JWT token for authentication
     * @return a ResponseEntity indicating the result of the deletion
     */
    @DeleteMapping("/delete/{workspaceId}")
    public ResponseEntity<ApiResponse<String>> deleteWorkspace(@PathVariable Long workspaceId,
                                                               @CookieValue("jwt") String token) {
        log.info("Received request to delete workspace with ID: {}", workspaceId);
        // TODO: Implement the delete workspace logic in the service
        throw new UnsupportedOperationException("This feature is not implemented yet");
    }
}
