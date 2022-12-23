package com.bookretail.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import com.bookretail.dto.auth.RegisterDto;

@Getter
public class UserDto extends RegisterDto {
    @ApiModelProperty
    private final Long companyUserId;

    @ApiModelProperty(example = "COMPANY_ADMIN")
    private final String role;

    public UserDto(Long companyUserId, String role, Long id, String name, String surname, String email, String phone) {
        super(id, name, surname, email, phone);
        this.role = role;
        this.companyUserId = companyUserId;
    }
}
