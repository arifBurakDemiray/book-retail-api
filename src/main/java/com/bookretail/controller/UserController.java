package com.bookretail.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.bookretail.dto.Response;
import com.bookretail.dto.auth.ProfileDto;
import com.bookretail.dto.user.ProfilePictureUpdateDto;
import com.bookretail.service.UserService;

@RestController
@Api(tags = UserController.tag)
@RequestMapping(UserController.tag)
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
public class UserController {

    public static final String description = "User related endpoints and CRUD operations";
    public static final String tag = "users";

    private final UserService userService;
    
    @GetMapping("me")
    @ApiOperation(value = "Get user's profile info.")
    public ResponseEntity<Response<ProfileDto>> getProfile(
            @ApiParam(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token
    ) {
        return userService.getProfile(token).toResponseEntity();
    }

    @RequestMapping(
            path = "/me/picture",
            method = RequestMethod.PUT,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @ApiOperation(value = "Update user's profile picture.")
    public ResponseEntity<Response<ProfilePictureUpdateDto>> getProfilePictureUrl(
            @ApiParam(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token,
            @RequestPart("file") MultipartFile file
    ) {
        return userService.updateProfilePicture(token, file).toResponseEntity();
    }
}
