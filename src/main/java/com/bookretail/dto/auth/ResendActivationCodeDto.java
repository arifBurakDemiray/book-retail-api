package com.bookretail.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResendActivationCodeDto {
    @Schema(example = "Successful")
    private final String message;
}
