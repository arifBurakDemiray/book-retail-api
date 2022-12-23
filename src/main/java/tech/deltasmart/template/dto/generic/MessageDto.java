package com.bookretail.dto.generic;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public abstract class MessageDto {

    @ApiModelProperty(example = "Successful")
    private String message;
}