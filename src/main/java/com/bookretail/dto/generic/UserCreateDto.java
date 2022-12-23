package com.bookretail.dto.generic;

import com.bookretail.util.ReadingIsGoodRegex;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {
    @Schema(example = "Ali")
    private String name;

    @Schema(example = "Veli")
    private String surname;

    @Email(regexp = ReadingIsGoodRegex.EMAIL, message = "{validation.generic.email.unfit_regex}")
    @Schema(required = true, example = "ali.veli@example.com")
    private String email;

    @Schema(example = "5079696533")
    @Pattern(regexp = ReadingIsGoodRegex.PHONE, message = "{validation.generic.phone.unfit_regex}")
    private String phone;

    @Positive
    @Schema
    private Long parkingId;
}
