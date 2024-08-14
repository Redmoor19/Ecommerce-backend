package com.example.gameStore.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
public class GlobalResponse<T> {
    public final static String SUCCESS = "success";
    public final static String ERROR = "error";

    private final T data;
    private final String status;
    private final Error error;

    public GlobalResponse(T data, String status, Integer errorCode, String errorMessage) {
        this.data = data;
        this.status = status;
        if(errorCode == null || errorMessage == null) {
            this.error = null;
            return;
        }
        this.error = new Error(errorMessage, errorCode);
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
