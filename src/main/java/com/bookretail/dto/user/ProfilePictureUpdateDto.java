package com.bookretail.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProfilePictureUpdateDto {
    @ApiModelProperty(example = "https://example.com/img.png")
    private String url;
}
