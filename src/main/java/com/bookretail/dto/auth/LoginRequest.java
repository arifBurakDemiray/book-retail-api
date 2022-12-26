package com.bookretail.dto.auth;

import com.bookretail.enums.EGrantType;
import com.bookretail.validator.annotation.ClientId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class LoginRequest {
    @NotNull(message = "{login_request.grant_type.empty}")
    @Schema(required = true, example = "password")
    public final EGrantType grant_type;

    @ClientId
    @NotBlank(message = "{login_request.client_id.empty}")
    @Schema(required = true, example = "26dc8857-600c-464e-8ec1-062480b01592")
    public final String client_id;

    @Schema(example = "26dc8857-600c-464e-8ec1-062480b01592")
    public final String client_secret;

    @Schema(example = "ali.veli@example.com")
    @Getter
    public final String username;

    @Schema(example = "p4ssw0rd")
    public final String password;

    @Schema
    public final String refresh_token;
}
