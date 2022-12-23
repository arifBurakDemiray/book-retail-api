package com.bookretail.dto.auth;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {
    @ApiModelProperty(required = true, example = "p4ssw0rd123")
    @Size(min = 4, max = 32, message = "{reset_password_request.password.invalid}")
    private String password;

    @NotBlank(message = "{reset_password_request.code.empty}")
    @ApiModelProperty(required = true, example = "26dc8857-600c-464e-8ec1-062480b01592")
    private String code;
}
