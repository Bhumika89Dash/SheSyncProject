    package com.patientmanage.patientservice.kafka;

    import com.patientmanage.patientservice.model.Patient;
    import patient.events.PatientEvent;

    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.kafka.core.KafkaTemplate;
    import org.springframework.stereotype.Service;

    @Service
    public class KafkaProducer {

        private static final Logger log = LoggerFactory.getLogger(
                KafkaProducer.class);
        private final KafkaTemplate<String, byte[]> kafkaTemplate;

        public KafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
            this.kafkaTemplate = kafkaTemplate;
        }

        public void sendEvent(Patient patient) {
            try {
                PatientEvent event = PatientEvent.newBuilder()
                        .setPatientId(patient.getId().toString())
                        .setName(patient.getName())
                        .setEmail(patient.getEmail())
                        .setEventType("PATIENT_CREATED")
                        .build();

                kafkaTemplate.send("patient", event.toByteArray()); // blocking send
                log.info("✅ Event sent successfully for patient {}", patient.getId());
            } catch (Exception ex) {
                log.error("❌ Failed to send PatientCreated event", ex);
                throw new RuntimeException("Kafka send failed", ex); // this will show in 500 logs
            }
        }

    }



    /*public void sendEvent(Patient patient) {

            PatientEvent event = PatientEvent.newBuilder()
                    .setPatientId(patient.getId().toString())
                    .setName(patient.getName())
                    .setEmail(patient.getEmail())
                    .setEventType("PATIENT_CREATED")
                    .build();

            kafkaTemplate.send("patient", event.toByteArray())
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("❌ Failed to send PatientCreated event", ex);
                        } else {
                            log.info("✅ Event sent to topic={}, offset={}",
                                    result.getRecordMetadata().topic(),
                                    result.getRecordMetadata().offset());
                        }
                    });

        }*/
