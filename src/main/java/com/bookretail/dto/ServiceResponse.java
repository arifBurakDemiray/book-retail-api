package com.bookretail.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class ServiceResponse<T> {

    @Getter
    private T data = null;

    @Getter
    private String message = null;

    private ServiceResponse(@NotNull T data) {
        this.data = data;
    }

    private ServiceResponse(@NotNull String message) {
        this.message = message;
    }

    public static <K> ServiceResponse<K> ok(@NotNull K data) {
        return new ServiceResponse<>(data);
    }

    public static <K> ServiceResponse<K> notOk(@NotNull String message) {
        return new ServiceResponse<>(message);
    }

    @JsonIgnore
    public boolean isOk() {
        return message == null;
    }

    @JsonIgnore
    public boolean isNotOk() {
        return message != null;
    }

}
