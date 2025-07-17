package org.spring.metaquery.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.metaquery.dto.*;
import org.spring.metaquery.dto.auth.LoginRequest;
import org.spring.metaquery.dto.auth.LoginResponse;
import org.spring.metaquery.dto.auth.SignUpRequest;
import org.spring.metaquery.dto.auth.SignUpResponse;
import org.spring.metaquery.entities.User;
import org.spring.metaquery.security.JwtService;
import org.spring.metaquery.security.UserPrincipal;
import org.spring.metaquery.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController handles user authentication operations such as signup, login, and logout.
 * It uses the AuthenticationManager to authenticate users and the JwtService to generate JWT tokens.
 * The controller also interacts with the UserService to manage user data.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    /**
     * Handles user signup by checking if the user already exists and saving a new user if not.
     * Returns a response with the user's details or an error message if the user already exists.
     *
     * @param request The signup request containing user details.
     * @return ResponseEntity with the user's details or an error message.
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<?>> signup(@RequestBody SignUpRequest request) {
        log.info("Received signup request for email: {}", request.email());
        if (userService.checkIfUserExists(request.email())) {
            log.warn("User with email {} already exists", request.email());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>(false, "User already exists"));
        }

        User user = userService.saveNewUser(request);
        SignUpResponse signUpResponse = new SignUpResponse(user.getId(), user.getUsername(), user.getEmail(), user.getProfileImageUrl());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "User created successfully", signUpResponse));
    }

    /**
     * Handles user login by authenticating the user and generating a JWT token.
     * Returns a response with the user's details and sets the JWT token in an HTTP-only cookie.
     *
     * @param request  The login request containing email and password.
     * @param response The HTTP response to set the cookie.
     * @return ResponseEntity with the user's details or an error message if authentication fails.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        log.info("Received login request for email: {}", request.email());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            String jwtToken = jwtService.generateToken(userPrincipal);

            // Create HTTP-only cookie
            ResponseCookie cookie = ResponseCookie.from("jwt", jwtToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(24 * 60 * 60)
                    .sameSite("Strict")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            User user = userService.getUserByEmail(userPrincipal.getUsername());
            log.info("User {} logged in successfully", user.getUsername());

            LoginResponse loginResponse = new LoginResponse(user.getId(), user.getUsername(), user.getEmail(), user.getProfileImageUrl());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse<>(true, "User logged in successfully", loginResponse));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Invalid email or password"));
        }
    }

    /**
     * Handles user logout by clearing the JWT cookie.
     * Returns a response indicating successful logout.
     *
     * @param response The HTTP response to set the cookie.
     * @return ResponseEntity indicating successful logout.
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(HttpServletResponse response) {
        log.info("Received logout request");
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "User logged out successfully"));
    }
}
