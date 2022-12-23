package com.bookretail.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.bookretail.enums.EErrorCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
    public final String accessToken;

    public final String refreshToken;

    public final EErrorCode code;
    public final String message;

    @JsonIgnore
    private final boolean success;

    private LoginResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;

        message = null;
        code = null;
        success = true;
    }

    private LoginResponse(String message, EErrorCode code) {
        this.message = message;
        this.code = code;

        accessToken = null;
        success = false;
        refreshToken = null;
    }

    public static LoginResponse ok(String accessToken, String refreshToken) {
        return new LoginResponse(accessToken, refreshToken);
    }

    public static LoginResponse notOk(String message, EErrorCode code) {
        return new LoginResponse(message, code);
    }

    public boolean isSuccess() {
        return success;
    }
}
