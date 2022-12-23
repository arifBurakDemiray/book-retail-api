package com.bookretail.controller;

import com.bookretail.dto.Response;
import com.bookretail.dto.auth.*;
import com.bookretail.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@Tag(name = AuthController.tag, description = AuthController.description)
@RequestMapping(AuthController.tag)
@AllArgsConstructor
public class AuthController {
    public static final String description = "Authentication related endpoints.";
    public static final String tag = "auth";

    private final AuthService authService;

    @PreAuthorize("isAnonymous()")
    @RequestMapping(
            method = RequestMethod.POST,
            path = "login",
            consumes = {
                    MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                    MediaType.MULTIPART_FORM_DATA_VALUE
            },
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "Login a user using password or refresh token.",
            description = "If grant_type is password, username, password, and client_secret fields are required. " +
                    "If grant_type is refresh_token, only refresh_token is required. "
    )
    public ResponseEntity<LoginResponse> login(
            @Valid @ModelAttribute LoginRequest body,
            @NotNull BindingResult bindingResult
    ) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        var result = authService.login(body);

        return result.isSuccess() ?
                ResponseEntity.ok(result) :
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("register")
    @Operation(
            summary = "Register a new user",
            description = "Please follow validation constraints on Schema, only TR phone numbers are valid for now."
    )
    public ResponseEntity<Response<RegisterDto>> registerUser(@Valid @RequestBody RegisterRequest body) {
        return authService.register(body).toResponseEntity();
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("resend-activation-code")
    @Operation(
            summary = "Resend activation code to user."
    )
    public ResponseEntity<Response<ResendActivationCodeDto>> resendActivationCode(
            @Valid @RequestBody ResendActivationCodeRequest body
    ) {
        return authService.resendActivationCode(body).toResponseEntity();
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("activate")
    @Operation(
            summary = "Activate user account."
    )
    public ResponseEntity<Response<AccountActivationDto>> activateAccount(
            @Valid @RequestBody AccountActivationRequest body
    ) {
        return authService.activateAccount(body).toResponseEntity();
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("forgot-password")
    @Operation(
            summary = "Send a forgot password email to user.",
            description = "This endpoint sends an email to user to reset user's password"
    )
    public ResponseEntity<Response<ForgotPasswordDto>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest body) {
        return authService.forgotPassword(body).toResponseEntity();
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("reset-password")
    @Operation(
            summary = "Reset user's password by a new password.",
            description = "This endpoint resets user's password"
    )
    public ResponseEntity<Response<ResetPasswordDto>> resetPassword(@Valid @RequestBody ResetPasswordRequest body) {
        return authService.resetPassword(body).toResponseEntity();
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    @Operation(summary = "Logout from a user device")
    public ResponseEntity<Response<LogoutResponse>> logout(
            @Valid @NotBlank(message = "{logout.device_id.blank}")
            @RequestParam(name = "device_id") String deviceId,
            @Parameter(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token) {
        return authService.logout(token, deviceId).toResponseEntity();
    }
}
