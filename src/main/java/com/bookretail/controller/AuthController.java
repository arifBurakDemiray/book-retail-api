package com.bookretail.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
import com.bookretail.dto.Response;
import com.bookretail.dto.auth.*;
import com.bookretail.service.AuthService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@Api(tags = AuthController.tag)
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
    @ApiOperation(
            value = "Login a user using password or refresh token.",
            notes = "If grant_type is password, username, password, and client_secret fields are required. " +
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
    @ApiOperation(
            value = "Register a new user",
            notes = "Please follow validation constraints on Schema, only TR phone numbers are valid for now."
    )
    public ResponseEntity<Response<RegisterDto>> registerUser(@Valid @RequestBody RegisterRequest body) {
        return authService.register(body).toResponseEntity();
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("resend-activation-code")
    @ApiOperation(
            value = "Resend activation code to user."
    )
    public ResponseEntity<Response<ResendActivationCodeDto>> resendActivationCode(
            @Valid @RequestBody ResendActivationCodeRequest body
    ) {
        return authService.resendActivationCode(body).toResponseEntity();
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("activate")
    @ApiOperation(
            value = "Activate user account."
    )
    public ResponseEntity<Response<AccountActivationDto>> activateAccount(
            @Valid @RequestBody AccountActivationRequest body
    ) {
        return authService.activateAccount(body).toResponseEntity();
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("forgot-password")
    @ApiOperation(
            value = "Send a forgot password email to user.",
            notes = "This endpoint sends an email to user to reset user's password"
    )
    public ResponseEntity<Response<ForgotPasswordDto>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest body) {
        return authService.forgotPassword(body).toResponseEntity();
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("reset-password")
    @ApiOperation(
            value = "Reset user's password by a new password.",
            notes = "This endpoint resets user's password"
    )
    public ResponseEntity<Response<ResetPasswordDto>> resetPassword(@Valid @RequestBody ResetPasswordRequest body) {
        return authService.resetPassword(body).toResponseEntity();
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    @ApiOperation(value = "Logout from a user device")
    public ResponseEntity<Response<LogoutResponse>> logout(
            @Valid @NotBlank(message = "{logout.device_id.blank}")
            @RequestParam(name = "device_id") String deviceId,
            @ApiParam(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token) {
        return authService.logout(token, deviceId).toResponseEntity();
    }
}
