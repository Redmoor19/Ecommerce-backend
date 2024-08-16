package com.example.gameStore.services.interfaces;

import java.util.List;
import java.util.Map;

public interface EmailService {
    void sendMessagePurchasedKeys(String email, Map<String, List<String>> keys);

    void sendMessageAccountVerification(String email, String token, String hostUrl);

    void sendMessagePasswordReset(String email, String token, String hostUrl);
}
