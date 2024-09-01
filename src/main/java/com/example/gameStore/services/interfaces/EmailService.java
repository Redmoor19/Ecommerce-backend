package com.example.gameStore.services.interfaces;

import com.example.gameStore.entities.User;

import java.util.List;
import java.util.Map;

public interface EmailService {
    void sendMessagePurchasedKeys(User user, Map<String, List<String>> keys);

    void sendMessageAccountVerification(User user, String token, String hostUrl);

    void sendMessagePasswordReset(User user, String token, String hostUrl);
}
