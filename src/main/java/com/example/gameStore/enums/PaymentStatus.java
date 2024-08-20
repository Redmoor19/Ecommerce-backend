package com.example.gameStore.enums;

import java.util.ArrayList;
import java.util.List;

public enum PaymentStatus {
    WAITING("WAITING"),
    PAID("PAID"),
    REJECTED("REJECTED"),
    UNPAID("UNPAID");

    private final String name;

    PaymentStatus(String name) {
        this.name = name;
    }

    public static boolean isValidType(String type) {
        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.name.equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }

    public static List<String> getAllPaymentStatuses() {
        List<String> arrayList = new ArrayList<>();
        for (PaymentStatus s : PaymentStatus.values()) {
            arrayList.add(s.name);
        }
        return arrayList;
    }
}
