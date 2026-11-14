package edu.ncsu.acm.studygroup.controller;

import edu.ncsu.acm.studygroup.dto.AuthResponseDto;
import edu.ncsu.acm.studygroup.dto.LoginDto;
import edu.ncsu.acm.studygroup.dto.SignupDto;
import edu.ncsu.acm.studygroup.entity.User;
import edu.ncsu.acm.studygroup.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Provides REST API endpoints for signup and login.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Creates the authentication controller.
     *
     * @param authService service used for authentication
     */
    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    /**
     * Creates a new account.
     *
     * @param signupDto account information
     * @return authenticated user information
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @Valid @RequestBody final SignupDto signupDto) {

        try {
            final User user = authService.signup(signupDto);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(toResponse(user));

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest()
                    .body(exception.getMessage());
        }
    }

    /**
     * Authenticates an existing account.
     *
     * @param loginDto login credentials
     * @return authenticated user information
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody final LoginDto loginDto) {

        return authService.login(loginDto)
                .<ResponseEntity<?>>map(user ->
                        ResponseEntity.ok(toResponse(user)))
                .orElseGet(() ->
                        ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body("Invalid email or password."));
    }

    /**
     * Converts a User into the safe authentication response sent
     * to the frontend.
     *
     * @param user authenticated user
     * @return authentication response
     */
    private AuthResponseDto toResponse(final User user) {
        return new AuthResponseDto(
                user.getUserId(),
                user.getName(),
                user.getEmail()
        );
    }
}