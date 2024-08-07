package com.example.gameStore.enums;

public enum UserRole {
    ADMIN("ADMIN"),
    USER("USER");

    private final String name;

    UserRole(String name) {
        this.name = name;
    }

    public static boolean isValidRole(String checkingRole) {
        for (UserRole role: UserRole.values()) {
            if(role.name.equalsIgnoreCase(checkingRole)) {
                return true;
            }
        }
        return false;
    }

}
