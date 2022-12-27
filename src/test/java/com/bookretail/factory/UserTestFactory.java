package com.bookretail.factory;

import com.bookretail.dto.auth.LoginRequest;
import com.bookretail.dto.auth.ProfileDto;
import com.bookretail.dto.auth.RegisterDto;
import com.bookretail.dto.auth.RegisterRequest;
import com.bookretail.enums.EGrantType;
import com.bookretail.enums.ERole;
import com.bookretail.model.User;

import java.util.Date;
import java.util.UUID;


public class UserTestFactory {

    public UserTestFactory() {
    }

    public static User createFailTestUserWithRole(String role) {
        User user = new User();
        user.setName("test");
        user.setSurname("test");
        user.setPassword("test");
        user.setEmail("test@test.com");
        user.setRole(ERole.valueOf(role));

        return user;
    }

    public static User createSuccessTestUser_USER() {
        User user = new User();
        user.setName("test");
        user.setSurname("test");
        user.setPassword("p4ssw0rd123");
        user.setEmail("deneme@test.com");
        user.setRole(ERole.valueOf(ERole.USER));
        user.setId(1L);

        return user;
    }

    public static String token_USER = "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIxIiwic3ViIjoiZGVuZW1lQHRlc3QuY29tIiwiaWF0IjoxNjcyMDQ3ODk4LCJleHAiOjE2NzIwNjk0OTgsInJvbGVJZCI6MX0.BN7Flirmw7hN0D4x99-Pa-QStRmaPhKPT1cMOejyQXw82FfyM_fVEZplZFKIBqGb2Pun0Ov7tzljgDiHJEwgJQ";


    public static String profile_photo_USER = "http://localhost:5000/profile_picture.png";

    public static RegisterRequest createRegisterRequest() {
        return new RegisterRequest("Ali", "Veli", "ali.veli@example.com",
                "p4ssw0rd123", "5079696533");
    }

    public static RegisterDto createRegisterDto() {
        return new RegisterDto(123L, "Ali", "Veli", "ali.veli@example.com", "5079696533");
    }

    public static String createRandomCode() {
        return UUID.randomUUID().toString();
    }

    public static LoginRequest createLoginRequest() {
        return new LoginRequest(EGrantType.password, "1da989ab-3d4e-4d79-a79e-5ca2e7b6345e",
                "client secret", "username", "p4ssw0rd", "refresh token");
    }

    public static ProfileDto createProfileDto(Date resultCreatedAt) {
        return new ProfileDto(123L, "name", "surname", "me@example.com", "5073681699",
                resultCreatedAt, "https://example.com/abc.jpg", "USER");
    }

    public static ProfileDto createProfileDto(User user) {
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

    public static User createSuccessTestUser_SYSADMIN() {
        User user = new User();
        user.setName("test");
        user.setSurname("test");
        user.setPassword("p4ssw0rd123");
        user.setEmail("deneme1@test.com");
        user.setRole(ERole.valueOf(ERole.SYSADMIN));
        user.setId(3L);

        return user;
    }

    public static LoginRequest createLoginRequest(EGrantType grantType) {
        return new LoginRequest(
                grantType,
                "client_id",
                "secret",
                "username",
                "password",
                "ABC123");
    }

    public static LoginRequest createLoginRequestSecretNull(EGrantType grantType) {
        return new LoginRequest(
                grantType,
                "client_id",
                null,
                "username",
                "password",
                "ABC123");
    }
}
