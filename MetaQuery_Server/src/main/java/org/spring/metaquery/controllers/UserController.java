package org.spring.metaquery.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.metaquery.dto.*;
import org.spring.metaquery.dto.user.UpdateUserPasswordRequest;
import org.spring.metaquery.dto.user.UpdateUserPasswordResponse;
import org.spring.metaquery.dto.user.UpdateUsernameRequest;
import org.spring.metaquery.dto.user.UpdateUsernameResponse;
import org.spring.metaquery.entities.User;
import org.spring.metaquery.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * UserController is a REST controller that handles user-related operations.
 * It provides endpoints for updating user information such as username, password, and profile image.
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Updates the username of the authenticated user.
     *
     * @param request The request containing the new username.
     * @param token   The JWT token of the authenticated user.
     * @return A response entity containing the updated username and timestamp.
     */
    @PatchMapping("/update-username")
    public ResponseEntity<ApiResponse<?>> updateUsername(@RequestBody UpdateUsernameRequest request, @CookieValue(
            "jwt") String token) {
        log.info("Received request to update username : {}", request);
        if (request.newUsername() == null || request.newUsername().isEmpty()) {
            log.warn("New username is empty");
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "New username cannot be empty"));
        }

        User updatedUser = userService.updateUsername(request.newUsername(), token);
        log.info("Update username successful: {}", updatedUser);
        return ResponseEntity.ok(new ApiResponse<>(true, "Username updated successfully",
                new UpdateUsernameResponse(updatedUser.getUsername(), updatedUser.getUpdatedAt())));
    }

    /**
     * Updates the password of the authenticated user.
     *
     * @param request The request containing the new password.
     * @param token   The JWT token of the authenticated user.
     * @return A response entity containing the updated timestamp.
     */
    @PatchMapping("/update-password")
    public ResponseEntity<ApiResponse<?>> updatePassword(@RequestBody UpdateUserPasswordRequest request, @CookieValue(
            "jwt") String token) {
        log.info("Received request to update password");
        if (request.newPassword() == null || request.newPassword().isEmpty()) {
            log.warn("New password is empty");
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Password cannot be empty"));
        }

        User updatedUser = userService.updatePassword(request.newPassword(), token);
        log.info("Update password successful: {}", updatedUser);
        return ResponseEntity.ok(new ApiResponse<>(true, "Password updated successfully",
                new UpdateUserPasswordResponse(updatedUser.getUpdatedAt())));
    }

    /**
     * Updates the profile image of the authenticated user.
     *
     * @param imageUrl The URL of the new profile image.
     * @param token    The JWT token of the authenticated user.
     * @return A response entity indicating success or failure.
     */
    @PatchMapping("/update-profile-image")
    public ResponseEntity<ApiResponse<?>> updateProfileImage(
            @RequestParam("imageUrl") String imageUrl,
            @CookieValue("jwt") String token) {
        log.info("Received request to update profile image: {}", imageUrl);
        // TODO: Implement profile image update logic using imageUrl and token
        throw new UnsupportedOperationException("This feature is not implemented yet");
    }
}