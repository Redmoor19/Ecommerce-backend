package com.example.gameStore.utilities;

import java.util.UUID;

public class GameUtilities {
    public static String generateSku() {
        UUID uniqueId = UUID.randomUUID();
        return "SKU-" + uniqueId.toString();
    }
}
