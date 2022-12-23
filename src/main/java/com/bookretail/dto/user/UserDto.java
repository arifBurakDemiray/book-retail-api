package com.bookretail.dto.user;

import com.bookretail.dto.auth.RegisterDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UserDto extends RegisterDto {
    @Schema
    private final Long companyUserId;

    @Schema(example = "COMPANY_ADMIN")
    private final String role;

    public UserDto(Long companyUserId, String role, Long id, String name, String surname, String email, String phone) {
        super(id, name, surname, email, phone);
        this.role = role;
        this.companyUserId = companyUserId;
    }
}
