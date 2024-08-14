package com.example.gameStore.utilities;

import java.util.UUID;

public class TypeConverter {
    public static UUID convertStringToUUID(String input, String errorMessage) {
        try {
            return UUID.fromString(input);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
