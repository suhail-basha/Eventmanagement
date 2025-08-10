package com.example.EventManageProject.Services;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import  com.example.EventManageProject.Service.EmailService;

class EmailServiceTest {

    private JavaMailSender mailSender;
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        mailSender = mock(JavaMailSender.class);
        emailService = new EmailService(mailSender);
    }

    @Test
    @DisplayName("Should send registration confirmation email with correct content")
    void testSendRegistrationConfirmation() {
        // Given
        String toEmail = "test@example.com";
        String eventTitle = "Spring Boot Workshop";

        // When
        emailService.sendRegistrationConfirmation(toEmail, eventTitle);

        // Then
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertThat(sentMessage.getTo()).containsExactly(toEmail);
        assertThat(sentMessage.getSubject()).isEqualTo("Event Registration Confirmed");
        assertThat(sentMessage.getText()).contains("Thank you for registering for the event: " + eventTitle);
    }
}

