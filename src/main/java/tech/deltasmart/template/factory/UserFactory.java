package com.bookretail.factory;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import com.bookretail.config.AppConfig;
import com.bookretail.dto.auth.ProfileDto;
import com.bookretail.dto.auth.RegisterDto;
import com.bookretail.dto.auth.RegisterRequest;
import com.bookretail.dto.user.UserUpdateRequest;
import com.bookretail.enums.ERole;
import com.bookretail.model.User;

import java.util.Locale;


@Component
@AllArgsConstructor
public class UserFactory {
    private final BCryptPasswordEncoder encoder;
    private final AppConfig appConfig;

    private String getDefaultProfilePictureUrl() {
        return appConfig.getUrl() + "/static/img/profile-picture.png";
    }

    public User createUser(@NotNull RegisterRequest dto) {
        return new User(
                dto.getName(),
                dto.getSurname(),
                dto.getEmail().toLowerCase(Locale.US),
                dto.getPhone(),
                encoder.encode(dto.getPassword()),
                ERole.valueOf(ERole.USER),
                getDefaultProfilePictureUrl()
        );
    }

    public ProfileDto createProfileDto(@NotNull User user) {
        return new ProfileDto(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getCreatedOn(),
                user.getProfilePicture(),
                ERole.stringValueOf(user.getRole())
        );
    }

    public RegisterDto createRegisterDto(User user) {
        return new RegisterDto(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getUsername(),
                user.getPhoneNumber()
        );
    }

    private String extractRoleName(int roleId) {
        var role = ERole.stringValueOf(roleId);
        return role.substring(role.indexOf("_") + 1);
    }

    public User createUser(User user, UserUpdateRequest body) {
        return new User(
                user.getId(),
                body.getName(),
                body.getSurname(),
                body.getEmail().toLowerCase(Locale.US),
                body.getPhone(),
                user.getPassword(),
                user.getRole(),
                user.getProfilePicture(),
                user.getCreatedOn(),
                !user.isEnabled(),
                user.isAccountNonLocked()
        );
    }
}
