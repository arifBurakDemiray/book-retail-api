package com.bookretail.dto.auth;

import com.bookretail.util.ReadingIsGoodRegex;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RegisterRequest {

    @Schema(example = "Ali")
    @Size(min = 1, max = 255, message = "{register_request.name.empty}")
    private String name;

    @Schema(example = "Veli")
    @Size(min = 1, max = 255, message = "{register_request.surname.empty}")
    private String surname;

    @Email(regexp = ReadingIsGoodRegex.EMAIL, message = "{validation.generic.email.unfit_regex}")
    @Schema(required = true, example = "ali.veli@example.com")
    private String email;

    @Getter
    @Schema(required = true, example = "p4ssw0rd123")
    @Size(min = 4, max = 32, message = "{register_request.password.invalid}")
    private String password;

    @Schema(example = "5079696533")
    @Pattern(regexp = ReadingIsGoodRegex.PHONE, message = "{validation.generic.phone.unfit_regex}")
    private String phone;
}
