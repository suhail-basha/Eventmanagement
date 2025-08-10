package com.example.EventManageProject.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendRegistrationConfirmation(String toEmail, String eventTitle) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Event Registration Confirmed");
        message.setText("Thank you for registering for the event: " + eventTitle + "\n\nSee you there!");
        mailSender.send(message);
    }
}
