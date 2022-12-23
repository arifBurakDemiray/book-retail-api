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
public class AccountActivationRequest {

    @NotBlank(message = "{activation.code.empty}")
    @ApiModelProperty(required = true, example = "26dc8857-600c-464e-8ec1-062480b01592")
    private String code;

    @Getter
    @ApiModelProperty(example = "p4ssw0rd123")
    @Size(min = 4, max = 32, message = "{validation.auth.register_request.password.invalid}")
    private final String password = null;

    @Getter
    @ApiModelProperty(example = "p4ssw0rd123")
    private final String password2 = null;
}
