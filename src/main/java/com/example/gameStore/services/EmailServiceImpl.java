package com.example.gameStore.services;

import com.example.gameStore.entities.User;
import com.example.gameStore.services.interfaces.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendMessagePurchasedKeys(User user, Map<String, List<String>> keys) {
        Map<String, Object> templateModel = new HashMap<>();

        templateModel.put("username", user.getName());
        templateModel.put("itemsMap", keys);

        sendMessage(user.getEmail(), "Games' keys", templateModel, "sendKeys");
    }

    public void sendMessageAccountVerification(User user, String token, String hostUrl) {
        Map<String, Object> templateModel = new HashMap<>();
        String url = hostUrl + "/verify/" + token;

        templateModel.put("username", user.getName());
        templateModel.put("verificationLink", url);

        sendMessage(user.getEmail(), "Welcome", templateModel, "welcome");
    }

    public void sendMessagePasswordReset(User user, String token, String hostUrl) {
        Map<String, Object> templateModel = new HashMap<>();
        String url = hostUrl + "/reset-password/" + token;

        templateModel.put("username", user.getName());
        templateModel.put("resetLink", url);

        sendMessage(user.getEmail(), "Password reset", templateModel, "passwordReset");
    }

    private void sendMessage(String email, String subject, Map<String, Object> templateModel, String templateName) {
        Context context = new Context();
        context.setVariables(templateModel);

        String htmlContent = templateEngine.process(templateName, context);
        MimeMessage message = emailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("fake.game.store.app@gmail.com");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            emailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
