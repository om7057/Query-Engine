package org.spring.metaquery.exceptions;

import org.spring.metaquery.dto.ApiResponse;
import org.spring.metaquery.exceptions.user.DuplicateUsernameException;
import org.spring.metaquery.exceptions.workspace.DuplicateWorkspaceNameException;
import org.spring.metaquery.exceptions.workspace.WorkspaceNotFoundException;
import org.spring.metaquery.exceptions.workspace.WorkspaceOwnershipException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for handling various exceptions in the application.
 * It provides a centralized way to handle exceptions and return appropriate responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<ApiResponse<?>> handleDuplicateUsernameException(DuplicateUsernameException ex) {
        ApiResponse<?> response = new ApiResponse<>(false, ex.getMessage(), null);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DuplicateWorkspaceNameException.class)
    public ResponseEntity<ApiResponse<?>> handleDuplicateWorkspaceNameException(DuplicateWorkspaceNameException ex) {
        ApiResponse<?> response = new ApiResponse<>(false, ex.getMessage(), null);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(WorkspaceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleWorkspaceNotFoundException(WorkspaceNotFoundException ex) {
        ApiResponse<?> response = new ApiResponse<>(false, ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(WorkspaceOwnershipException.class)
    public ResponseEntity<ApiResponse<?>> handleWorkspaceOwnershipException(WorkspaceOwnershipException ex) {
        ApiResponse<?> response = new ApiResponse<>(false, ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneralException(Exception ex) {
        ApiResponse<?> response = new ApiResponse<>(false, "An unexpected error occurred: " + ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
