package com.example.gameStore.services;

import com.example.gameStore.services.interfaces.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    private final String GET_KEYS_TEXT = """
            There are your keys for the game you have purchased: \n""";
    private final String GET_KEYS_SUBJECT = "Game keys";

    public void sendMessagePurchasedKeys(String email, Map<String, List<String>> keys) {
        String textKeys = keys.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining("\n"));

        sendMessage(email, GET_KEYS_SUBJECT, GET_KEYS_TEXT + textKeys);
    }

    private void sendMessage(String email, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("fake.game.store.app@gmail.com");
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}
