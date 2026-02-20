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
    public void consumeEvent(byte[] event, Acknowledgment ack) {

        try {
            PatientEvent patientEvent = PatientEvent.parseFrom(event);
            UUID patientId = UUID.fromString(patientEvent.getPatientId());

            log.info("Received Patient Event: {}", patientId);

            healthProfileService.createDefaultIfNotExists(patientId);
            symptomLogService.createDefaultIfNotExists(patientId);
            // VERY IMPORTANT LINE
            ack.acknowledge();

        } catch (Exception e) {
            log.error("Error processing patient event", e);
            // DO NOT ACKNOWLEDGE -> Kafka will retry
        }
    }

}