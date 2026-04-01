package com.patientManage.notification_service.service;

import com.patientManage.notification_service.dto.EmailRequest;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
   /* private JavaMailSender mailSender;
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
    }*/

    private static final Logger log = LoggerFactory.getLogger(EmailSenderService.class);

    @Value("${SENDGRID_API_KEY}")
    private String sendGridApiKey;

    @Value("${SENDER_EMAIL}")
    private String senderEmail;

    public void sendEmail(EmailRequest request) {

        log.info("Preparing to send email to {}", request.getRecipientEmail());

        try {
            Email from = new Email(senderEmail);
            Email to = new Email(request.getRecipientEmail());
            Content content = new Content("text/plain", request.getBody());

            Mail mail = new Mail(from, request.getSubject(), to, content);

            SendGrid sendGrid = new SendGrid(sendGridApiKey);
            Request sgRequest = new Request();

            sgRequest.setMethod(Method.POST);
            sgRequest.setEndpoint("mail/send");
            sgRequest.setBody(mail.build());

            log.debug("Sending request to SendGrid for recipient {}", request.getRecipientEmail());

            Response response = sendGrid.api(sgRequest);

            int statusCode = response.getStatusCode();

            if (statusCode >= 200 && statusCode < 300) {
                log.info("Email successfully sent to {}. SendGrid Status Code: {}",
                        request.getRecipientEmail(), statusCode);
            } else {
                log.error("SendGrid failed for {}. Status Code: {}, Response Body: {}",
                        request.getRecipientEmail(),
                        statusCode,
                        response.getBody());

                throw new RuntimeException("Email sending failed with status: " + statusCode);
            }

        } catch (Exception e) {
            log.error("Exception occurred while sending email to {}",
                    request.getRecipientEmail(), e);

            // Important: rethrow so Kafka retry mechanism works
            throw new RuntimeException("Email sending failed", e);
        }
    }
}