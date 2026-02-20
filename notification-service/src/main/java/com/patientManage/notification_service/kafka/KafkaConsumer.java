package com.patientManage.notification_service.kafka;
import com.google.protobuf.InvalidProtocolBufferException;
import com.patientManage.notification_service.dto.EmailRequest;
import com.patientManage.notification_service.service.EmailSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
public class KafkaConsumer {
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    private final EmailSenderService emailSenderService;

    public KafkaConsumer(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @KafkaListener(topics = "patient", groupId = "notification-group")
    public void consume(byte[] message) {

        try {
            PatientEvent event = PatientEvent.parseFrom(message);

            if (!event.getEventType().equals("PATIENT_CREATED"))
                return;

            if(event.getEmail() == null || event.getEmail().isBlank()){
                log.warn("Skipping event with empty email for patient {}", event.getPatientId());
                return;
            }

            log.info("Received Patient Created Event for {}", event.getEmail());

            EmailRequest email = EmailRequest.builder()
                    .recipientEmail(event.getEmail())
                    .subject("Welcome to SheSync 🌸")
                    .body(buildWelcomeMessage(event.getName()))
                    .build();

            emailSenderService.sendEmail(email);

        } catch (Exception e) {
            log.error("Failed to process patient event", e);
            throw new RuntimeException("Kafka retry: Email sending failed", e);
        }
    }

    private String buildWelcomeMessage(String name){
        return
                "Dear " + name + ",\n\n" +

                        "✨ Welcome to SheSync ✨\n\n" +

                        "We’re truly delighted to have you with us.\n\n" +

                        "SheSync is more than a tracker — it is a space designed for comfort, "
                        + "understanding, and awareness of your body’s natural rhythm.\n\n" +

                        "From cycle predictions to symptom insights and gentle reminders, "
                        + "our goal is simple: to help you feel informed, prepared, and cared for — every day.\n\n" +

                        "Your wellness journey is unique, and we’re honoured to walk alongside you in it.\n\n" +

                        "If you ever need guidance, reassurance, or support,\n"
                        + "we are always here for you.\n\n" +

                        "With warmth,\n"
                        + "— The SheSync Team 🌸\n\n" +

                        "Take care of yourself today.";
    }
    }
