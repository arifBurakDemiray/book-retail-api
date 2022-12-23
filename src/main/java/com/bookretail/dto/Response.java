package com.bookretail.dto;

import com.bookretail.enums.EErrorCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;


public class Response<T> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ErrorObject error = null;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data = null;

    private Response(@NotNull T data) {
        this.data = data;
    }

    private Response(@NotNull String message, @NotNull EErrorCode code) {
        error = new ErrorObject(message, code);
    }

    public static <K> Response<K> ok(K data) {
        return new Response<>(data);
    }

    public static <K> Response<K> notOk(String message, EErrorCode code) {
        return new Response<>(message, code);
    }

    @JsonIgnore
    public boolean isOk() {
        return error == null;
    }

    @JsonIgnore
    public boolean isNotOk() {
        return error != null;
    }

    public ErrorObject getError() {
        return error;
    }

    public T getData() {
        return data;
    }

    public ResponseEntity<Response<T>> toResponseEntity() {
        return isOk() ?
                ResponseEntity.ok(this) :
                ResponseEntity.status(error.code.httpStatusCode()).body(this);
    }

    private static class ErrorObject {

        @Schema(example = "400")
        private final EErrorCode code;
        @Schema(example = "Bad Request")
        private final String message;

        ErrorObject(String message, EErrorCode code) {
            this.code = code;
            this.message = message;
        }

        public EErrorCode getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
