package com.bookretail.dto.auth;

import com.bookretail.util.ReadingIsGoodRegex;
import com.bookretail.validator.annotation.ClientId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResendActivationCodeRequest {
    @NotBlank(message = "{forgot_password_request.client_id.empty}")
    @ClientId
    @Schema(required = true, example = "26dc8857-600c-464e-8ec1-062480b01592")
    private String clientId;

    @Email(regexp = ReadingIsGoodRegex.EMAIL, message = "{validation.generic.email.unfit_regex}")
    @Schema(required = true, example = "ali.veli@example.com")
    private String email;
}
