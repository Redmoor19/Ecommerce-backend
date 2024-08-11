package com.example.gameStore.services.interfaces;

import java.util.Map;

public interface EmailService {
    void sendMessagePurchasedKeys(String email, Map<String, String> keys);
}
