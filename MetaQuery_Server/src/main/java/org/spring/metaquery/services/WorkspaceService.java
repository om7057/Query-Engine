package org.spring.metaquery.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.metaquery.dto.workspace.NewWorkspaceRequest;
import org.spring.metaquery.dto.workspace.NewWorkspaceResponse;
import org.spring.metaquery.dto.workspace.WorkspaceInfoResponse;
import org.spring.metaquery.entities.User;
import org.spring.metaquery.entities.Workspace;
import org.spring.metaquery.exceptions.workspace.DuplicateWorkspaceNameException;
import org.spring.metaquery.exceptions.workspace.WorkspaceNotFoundException;
import org.spring.metaquery.exceptions.workspace.WorkspaceOwnershipException;
import org.spring.metaquery.repositories.WorkspaceRepository;
import org.spring.metaquery.security.JwtService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * WorkspaceService provides methods to manage workspace-related operations such as creating,
 * retrieving, and updating workspaces. It interacts with the WorkspaceRepository to perform
 * database operations and uses UserService for user-related functionalities.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceService {
    private final WorkspaceRepository workspaceRepository;
    private final UserService userService;
    private final JwtService jwtService;

    private Boolean checkIfWorkspaceExists(String workspaceName) {
        log.info("Checking if duplicate workspace exists: {}", workspaceName);
        return workspaceRepository.findByWorkspaceName(workspaceName).isPresent();
    }

    public Workspace getWorkspaceById(Long workspaceId) throws WorkspaceNotFoundException {
        log.info("Fetching workspace by ID: {}", workspaceId);
        return workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException("Workspace not found with ID: " + workspaceId));
    }

    public WorkspaceInfoResponse getWorkspaceById(Long workspaceId, String token) throws WorkspaceNotFoundException,
            WorkspaceOwnershipException {
        Long userId = jwtService.extractUserId(token);
        log.info("Checking if workspace exists: {}", workspaceId);
        Workspace workspace = workspaceRepository.findById(workspaceId).orElse(null);

        if (workspace == null) {
            log.error("Workspace not found with ID: {}", workspaceId);
            throw new WorkspaceNotFoundException("Workspace not found with ID: " + workspaceId);
        }

        if (workspace.getUser().getId().equals(userId)) {
            log.info("Workspace found for user with ID: {}", userId);
            return WorkspaceInfoResponse.fromEntity(workspace);
        } else {
            log.error("User with ID: {} does not own workspace with ID: {}", userId, workspaceId);
            throw new WorkspaceOwnershipException("Workspace not found for user with ID: " + userId);
        }
    }

    public List<WorkspaceInfoResponse> getAllWorkspaces(String token) throws WorkspaceNotFoundException,
            WorkspaceOwnershipException {
        Long userId = jwtService.extractUserId(token);
        log.info("Retrieving all workspaces for user with ID: {}", userId);
        User user = userService.getUserById(userId);

        List<Workspace> workspaces = workspaceRepository.findAllByUser(user);

        if (workspaces.isEmpty()) {
            log.error("No workspaces found for user with ID: {}", userId);
            throw new WorkspaceNotFoundException("No workspaces found for user with ID: " + userId);
        }

        return workspaces.stream()
                .map(WorkspaceInfoResponse::fromEntity)
                .toList();
    }


    public NewWorkspaceResponse createNewWorkspace(NewWorkspaceRequest request, String token) throws DuplicateWorkspaceNameException {
        if (checkIfWorkspaceExists(request.workspaceName())) {
            log.error("Duplicate workspace name found: {}", request.workspaceName());
            throw new DuplicateWorkspaceNameException("Workspace with name '" + request.workspaceName() + "' already " +
                    "exists.");
        }

        Long userId = jwtService.extractUserId(token);
        log.info("Creating new workspace for user with ID: {}", userId);
        User user = userService.getUserById(userId);

        Workspace workspace = Workspace.builder()
                .workspaceName(request.workspaceName())
                .workspaceDescription(request.workspaceDescription())
                .user(user)
                .build();

        workspaceRepository.save(workspace);
        log.info("Workspace created with name: {}", workspace.getWorkspaceName());

        return NewWorkspaceResponse.formEntity(workspace);
    }

    public WorkspaceInfoResponse updateWorkspaceDescription(
            Long workspaceId,
            String newDescription,
            String token) throws WorkspaceNotFoundException, WorkspaceOwnershipException {

        Long userId = jwtService.extractUserId(token);
        log.info("Updating workspace description for workspace ID: {}", workspaceId);

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException("Workspace not found with ID: " + workspaceId));

        if (!workspace.getUser().getId().equals(userId)) {
            log.error("User with ID: {} does not own workspace with ID for description update: {}", userId,
                    workspaceId);
            throw new WorkspaceOwnershipException("Workspace not found for user with ID: " + userId);
        }

        workspace.setWorkspaceDescription(newDescription);
        workspaceRepository.save(workspace);
        log.info("Workspace description updated for workspace ID: {}", workspaceId);

        return WorkspaceInfoResponse.fromEntity(workspace);
    }
}
