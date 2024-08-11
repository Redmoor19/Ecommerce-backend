package com.example.gameStore.services;

import com.example.gameStore.services.interfaces.EmailService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    private final String userText = """
            There are your keys for the game you have purchased:""";

    public void sendMessagePurchasedKeys(String email, Map<String, List<String>> keys) {

    }
}
