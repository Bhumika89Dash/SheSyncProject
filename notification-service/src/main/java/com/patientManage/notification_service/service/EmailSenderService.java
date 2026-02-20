package com.patientManage.notification_service.service;

import com.patientManage.notification_service.dto.EmailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    private JavaMailSender mailSender;
    private static final Logger log = LoggerFactory.getLogger(EmailSenderService.class);

    public EmailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.username}")
    private String senderEmail;

    public void sendEmail(EmailRequest request) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);
            message.setTo(request.getRecipientEmail());
            message.setSubject(request.getSubject());
            message.setText(request.getBody());

            mailSender.send(message);

            log.info("Email sent successfully to {}", request.getRecipientEmail());

        } catch (Exception e) {
            log.error("Mail sending failed to {}", request.getRecipientEmail(), e);

            // rethrow so kafka retries
            throw new RuntimeException("SMTP sending failed", e);
        }
    }
}