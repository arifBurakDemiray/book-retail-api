package com.bookretail.dto.generic;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.bookretail.util.ReadingIsGoodRegex;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {
    @ApiModelProperty(example = "Ali")
    private String name;

    @ApiModelProperty(example = "Veli")
    private String surname;

    @Email(regexp = ReadingIsGoodRegex.EMAIL, message = "{validation.generic.email.unfit_regex}")
    @ApiModelProperty(required = true, example = "ali.veli@example.com")
    private String email;

    @ApiModelProperty(example = "5079696533")
    @Pattern(regexp = ReadingIsGoodRegex.PHONE, message = "{validation.generic.phone.unfit_regex}")
    private String phone;

    @Positive
    @ApiModelProperty
    private Long parkingId;
}
