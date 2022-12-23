package com.bookretail.dto.auth;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResendActivationCodeDto {
    @ApiModelProperty(example = "Successful")
    private final String message;
}
