package com.bookretail.controller;

import com.bookretail.dto.Response;
import com.bookretail.dto.auth.ProfileDto;
import com.bookretail.dto.user.ProfilePictureUpdateDto;
import com.bookretail.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Tag(name = UserController.tag, description = UserController.description)
@RequestMapping(UserController.tag)
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
public class UserController {

    public static final String description = "User related endpoints and CRUD operations";
    public static final String tag = "users";

    private final UserService userService;

    @GetMapping("me")
    @Operation(summary = "Get user's profile info.")
    public ResponseEntity<Response<ProfileDto>> getProfile(
            @Parameter(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token
    ) {
        return userService.getProfile(token).toResponseEntity();
    }

    @RequestMapping(
            path = "/me/picture",
            method = RequestMethod.PUT,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(summary = "Update user's profile picture.")
    public ResponseEntity<Response<ProfilePictureUpdateDto>> getProfilePictureUrl(
            @Parameter(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token,
            @RequestPart("file") MultipartFile file
    ) {
        return userService.updateProfilePicture(token, file).toResponseEntity();
    }
}
