package com.bookretail.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProfilePictureUpdateDto {
    @Schema(example = "https://example.com/img.png")
    private String url;
}
