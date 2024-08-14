package com.example.gameStore.dtos;

import lombok.Getter;

@Getter
public class GlobalResponse<T> {
    private final T data;
    private final String message;

    public GlobalResponse(T data, String message) {
        this.data = data;
        this.message = message;
    }
}
