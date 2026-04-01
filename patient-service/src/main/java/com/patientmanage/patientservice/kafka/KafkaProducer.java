    package com.patientmanage.patientservice.kafka;

    import com.patientmanage.patientservice.model.Patient;
    import org.apache.kafka.clients.producer.RecordMetadata;
    import org.springframework.kafka.support.SendResult;
    import patient.events.PatientEvent;

    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.kafka.core.KafkaTemplate;
    import org.springframework.stereotype.Service;

    import java.util.concurrent.CompletableFuture;

    @Service
    public class KafkaProducer {

        private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
        private static final String TOPIC = "patient";

        private final KafkaTemplate<String, byte[]> kafkaTemplate;

        public KafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
            this.kafkaTemplate = kafkaTemplate;
            log.info("🚀 KafkaProducer initialized successfully");
        }

        public void sendEvent(Patient patient) {

            if (patient == null) {
                log.error("❌ Cannot send Kafka event: Patient object is null");
                return;
            }

            try {
                log.info("📤 Preparing Kafka event for patient ID={}", patient.getId());

                PatientEvent event = PatientEvent.newBuilder()
                        .setPatientId(patient.getId().toString())
                        .setName(patient.getName())
                        .setEmail(patient.getEmail())
                        .setEventType("PATIENT_CREATED")
                        .build();

                log.debug("🧾 Event payload size={} bytes", event.toByteArray().length);

                CompletableFuture<SendResult<String, byte[]>> future =
                        kafkaTemplate.send(TOPIC, event.toByteArray());

                future.whenComplete((result, ex) -> {

                    if (ex != null) {
                        log.error("❌ Kafka send FAILED for patient ID={}", patient.getId(), ex);
                    } else {
                        RecordMetadata metadata = result.getRecordMetadata();
                        log.info("✅ Kafka send SUCCESS");
                        log.info("📌 Topic: {}", metadata.topic());
                        log.info("📌 Partition: {}", metadata.partition());
                        log.info("📌 Offset: {}", metadata.offset());
                        log.info("📌 Timestamp: {}", metadata.timestamp());
                    }
                });

                log.info("📨 Kafka send request submitted (non-blocking)");

            } catch (Exception ex) {
                log.error("🔥 Unexpected error while creating Kafka event for patient ID={}",
                        patient.getId(), ex);
                // DO NOT THROW
            }
        }
    }







    /*@Service
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

*/

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
