package com.example.gameStore.services;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailServiceImpl {

    private final String userText = """
            There are your keys for the game you have purchased:""";

    void sendMessagePurchasedKeys(String email, Map<String, String> keys) {

    }
}
