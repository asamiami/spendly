package com.project.spendly2.services.implementation;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;


@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;



    public void sendMessage(String to, String subject, String body) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            String senderName = "Spendly!";
            helper.setTo(to);
            helper.setFrom("buddggoall@gmail.com", senderName);
            helper.setSubject(subject);
            helper.setText(body, true);

            javaMailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException  e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
