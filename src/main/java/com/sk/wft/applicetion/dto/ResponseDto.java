package com.sk.wft.applicetion.dto;

import lombok.Getter;

public record ResponseDto<T>(int status, String message, T data) {

    public static ResponseDto<?> success() {
        return success(null);
    }

    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(200, "OK", data);
    }

    public static ResponseDto<?> fail(int status, String message) {
        return new ResponseDto<>(status, message, null);
    }
}
