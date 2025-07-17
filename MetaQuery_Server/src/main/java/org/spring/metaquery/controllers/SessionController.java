package org.spring.metaquery.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.metaquery.dto.ApiResponse;
import org.spring.metaquery.dto.auth.LoginResponse;
import org.spring.metaquery.entities.User;
import org.spring.metaquery.security.JwtService;
import org.spring.metaquery.security.UserPrincipal;
import org.spring.metaquery.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SessionController handles session-related operations such as validating the current user session.
 * It provides an endpoint to check if the user is authenticated and returns user details if valid.
 */
@Slf4j
@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionController {
    private final JwtService jwtService;
    private final UserService userService;

    /**
     * Validates the current user session by checking the authentication context.
     * If the user is authenticated, it returns the user's details; otherwise, it returns an unauthorized error.
     *
     * @return ResponseEntity with user details or an error message.
     */
    @GetMapping("/validate-session")
    public ResponseEntity<ApiResponse<?>> validateSession() {
        log.info("Received request to validate user session");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof UserPrincipal userPrincipal)) {
            log.warn("Unauthorized access attempt");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Unauthorized access"));
        }

        User user = userService.getUserByEmail(userPrincipal.getUsername());
        LoginResponse loginResponse = new LoginResponse(user.getId(), user.getUsername(), user.getEmail(), user.getProfileImageUrl());
        log.info("User session validated successfully for user: {}", user.getUsername());
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new ApiResponse<>(true, "Session is valid", loginResponse));
    }
}
