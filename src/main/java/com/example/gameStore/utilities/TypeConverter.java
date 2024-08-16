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

    public static UUID convertStringToUUID(String input) {
        try {
            return UUID.fromString(input);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format: " + input);
        }
    }

    public static int convertStringToInt(String input, String errorMessage) {
        try {
            return Integer.parseInt(input);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static int convertStringToInt(String input) {
        try {
            return Integer.parseInt(input);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid INT format: " + input);
        }
    }
}
