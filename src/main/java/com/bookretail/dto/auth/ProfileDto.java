package com.bookretail.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDto {
    @Schema(example = "1")
    public final Long id;

    @Schema(example = "Johnny")
    public final String name;

    @Schema(example = "Cash")
    public final String surname;

    @Schema(example = "me@example.com")
    public final String email;

    @Schema(example = "5073681699")
    public final String phone;

    @Schema(example = "5073681699")
    public final Date registeredAt;

    @Schema(example = "https://example.com/abc.jpg")
    public final String profilePicture;

    private final String role;

    @JsonProperty("role")
    @Schema(example = "USER")
    private String serializeRole() {
        return role.startsWith("ROLE_") ? role.substring(5) : role;
    }
}
