package com.example.gameStore.dtos;

import lombok.Getter;

@Getter
public class GlobalResponse<T> {
    public final static String SUCCESS = "success";
    public final static String ERROR = "error";

    private final T data;
    private final String status;
    private final Error error;

    public GlobalResponse(Integer errorCode, String errorMessage) {
        this.data = null;
        this.status = ERROR;
        this.error = new Error(errorMessage, errorCode);
    }
    public GlobalResponse(T data) {
        this.data = data;
        this.status = SUCCESS;
        this.error = null;
    }

    @Getter
    public static class Error {
        private final String errorMessage;
        private final Integer errorCode;

        public Error(String message, Integer code) {
            this.errorMessage = message;
            this.errorCode = code;
        }
    }
}
