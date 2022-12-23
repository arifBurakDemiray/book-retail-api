package com.bookretail.dto.auth;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import com.bookretail.enums.EGrantType;
import com.bookretail.validator.annotation.ClientId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
public class LoginRequest {
    @NotNull(message = "{login_request.grant_type.empty}")
    @ApiModelProperty(required = true, example = "password")
    public final EGrantType grant_type;

    @ClientId
    @NotBlank(message = "{login_request.client_id.empty}")
    @ApiModelProperty(required = true, example = "26dc8857-600c-464e-8ec1-062480b01592")
    public final String client_id;

    @ApiModelProperty(example = "26dc8857-600c-464e-8ec1-062480b01592")
    public final String client_secret;

    @ApiModelProperty(example = "ali.veli@example.com")
    @Getter
    public final String username;

    @ApiModelProperty(example = "p4ssw0rd")
    public final String password;

    @ApiModelProperty
    public final String refresh_token;
}
