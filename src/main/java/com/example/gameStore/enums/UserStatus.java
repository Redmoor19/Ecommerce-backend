package com.example.gameStore.enums;

public enum UserStatus {
    ACTIVE("ACTIVE"),
    UNVERIFIED("UNVERIFIED"),
    NOT_ACTIVE("NOT_ACTIVE");

    private final String name;

    UserStatus(String name) {
        this.name = name;
    }

    public static boolean isValidStatus(String checkingStatus) {
        for (UserStatus status : UserStatus.values()) {
            if (status.name.equalsIgnoreCase(checkingStatus)) return true;
        }
        return false;
    }

}
