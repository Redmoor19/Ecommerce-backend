package com.example.gameStore.enums;

import java.util.ArrayList;
import java.util.List;

public enum OrderStatus {
    PROCESSING("PROCESSING"),
    APPROVED("APPROVED"),
    DECLINED("DECLINED"),
    DELIVERED("DELIVERED");

    private final String name;

    OrderStatus(String name) {
        this.name = name;
    }

    public static boolean isValidType(String type) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.name.equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }

    public static List<String> getAllOrderStatuses() {
        List<String> arrayList = new ArrayList<>();
        for (OrderStatus s : OrderStatus.values()) {
            arrayList.add(s.name);
        }
        return arrayList;
    }
}
