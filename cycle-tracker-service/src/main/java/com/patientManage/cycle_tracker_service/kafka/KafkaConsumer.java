package com.patientManage.cycle_tracker_service.kafka;
import com.google.protobuf.InvalidProtocolBufferException;
import com.patientManage.cycle_tracker_service.service.HealthProfileService;
import com.patientManage.cycle_tracker_service.service.SymptomLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

import java.util.UUID;

@Service
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    private final HealthProfileService healthProfileService;
    private final SymptomLogService symptomLogService;
    public KafkaConsumer(HealthProfileService healthProfileService, SymptomLogService symptomLogService) {
        this.healthProfileService = healthProfileService;
        this.symptomLogService = symptomLogService;
    }
    @KafkaListener(topics = "patient", groupId = "cycle-tracker-service")
    public void consumeEvent(byte[] event) {

        try {
            PatientEvent patientEvent = PatientEvent.parseFrom(event);

            UUID patientId = UUID.fromString(patientEvent.getPatientId());
            String name = patientEvent.getName();
            String email = patientEvent.getEmail();
            String eventType = patientEvent.getEventType();

            log.info("📥 Patient Event Consumed");
            log.info("📌 Event Type : {}", eventType);
            log.info("📌 Patient ID : {}", patientId);
            log.info("📌 Name       : {}", name);
            log.info("📌 Email      : {}", email);

            healthProfileService.createDefaultIfNotExists(patientId);
            symptomLogService.createDefaultIfNotExists(patientId);

            log.info("✅ Default HealthProfile and SymptomLog created for {}", patientId);

        } catch (Exception e) {
            log.error("❌ Error processing patient event", e);
        }
    }



    /*
    @KafkaListener(topics = "patient", groupId = "cycle-tracker-service")
    public void consumeEvent(byte[] event) {

        try {
            PatientEvent patientEvent = PatientEvent.parseFrom(event);
            UUID patientId = UUID.fromString(patientEvent.getPatientId());

            log.info("Received Patient Event: {}", patientId);

            healthProfileService.createDefaultIfNotExists(patientId);
            symptomLogService.createDefaultIfNotExists(patientId);



        } catch (Exception e) {
            log.error("Error processing patient event", e);

        }
    }*/

}