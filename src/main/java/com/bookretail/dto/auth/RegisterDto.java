package com.bookretail.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterDto {

    @ApiModelProperty(example = "0")
    public Long id;

    @ApiModelProperty(example = "Ali")
    public String name;

    @ApiModelProperty(example = "Veli")
    public String surname;

    @ApiModelProperty(required = true, example = "ali.veli@example.com")
    public String email;

    @ApiModelProperty(example = "5079696533")
    public String phone;

}
