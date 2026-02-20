package com.patientManage.analyticsService.kafka;
import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    private static final Logger log = LoggerFactory.getLogger(
            KafkaConsumer.class);

    @KafkaListener(topics="patient", groupId = "analytics-service") //group id tells the kafka broker who the consumer is
    public void consumeEvent(byte[] event) { //event array is of type byte array
        try {
            PatientEvent patientEvent = PatientEvent.parseFrom(event);
            // ... perform any business related to analytics here

            log.info("Received Patient Event: [PatientId={},PatientName={},PatientEmail={}]",
                    patientEvent.getPatientId(),
                    patientEvent.getName(),
                    patientEvent.getEmail());
        } catch (InvalidProtocolBufferException e) {
            log.error("Error deserializing event {}", e.getMessage());
        }
    }
}