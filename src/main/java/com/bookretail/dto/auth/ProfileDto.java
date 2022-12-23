package com.bookretail.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDto {
    @ApiModelProperty(example = "1")
    public final Long id;

    @ApiModelProperty(example = "Johnny")
    public final String name;

    @ApiModelProperty(example = "Cash")
    public final String surname;

    @ApiModelProperty(example = "me@example.com")
    public final String email;

    @ApiModelProperty(example = "5073681699")
    public final String phone;

    @ApiModelProperty(example = "5073681699")
    public final Date registeredAt;

    @ApiModelProperty(example = "https://example.com/abc.jpg")
    public final String profilePicture;

    private final String role;

    @JsonProperty("role")
    @ApiModelProperty(example = "USER")
    private String serializeRole() {
        return role.startsWith("ROLE_") ? role.substring(5) : role;
    }
}
