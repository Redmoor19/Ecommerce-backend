package com.example.gameStore.services.interfaces;

import java.util.List;
import java.util.Map;

public interface EmailService {
    void sendMessagePurchasedKeys(String email, Map<String, List<String>> keys);
}
