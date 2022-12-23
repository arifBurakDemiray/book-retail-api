package com.bookretail.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterDto {

    @Schema(example = "0")
    public Long id;

    @Schema(example = "Ali")
    public String name;

    @Schema(example = "Veli")
    public String surname;

    @Schema(required = true, example = "ali.veli@example.com")
    public String email;

    @Schema(example = "5079696533")
    public String phone;

}
