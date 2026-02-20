package com.patientManage.cycle_tracker_service.service;

import com.patientManage.cycle_tracker_service.dto.SymptomLogRequest;
import com.patientManage.cycle_tracker_service.exception.BadRequestException;
import com.patientManage.cycle_tracker_service.exception.NotFoundException;
import com.patientManage.cycle_tracker_service.model.HealthProfile;
import com.patientManage.cycle_tracker_service.model.SymptomLog;
import com.patientManage.cycle_tracker_service.repository.SymptomLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SymptomLogService {


    private static final Logger log = LoggerFactory.getLogger(SymptomLogService.class);

    private final SymptomLogRepository repository;

    public SymptomLogService(SymptomLogRepository repository) {
        this.repository = repository;
    }

    public void update(UUID patientId, SymptomLogRequest request) {
        if (request == null)
            throw new BadRequestException("Request body missing");

        SymptomLog profile = repository.findByPatientId(patientId)
                .orElseThrow(() -> new NotFoundException("Symptom Log not created yet"));

        boolean updated = false;

        if (request.getBloating() != null) {
            profile.setBloating(request.getBloating());
            updated = true;
        }

        if (request.getCramps() != null) {
            profile.setCramps(request.getCramps());
            updated = true;
        }

        if (request.getFatigue() != null) {
            profile.setFatigue(request.getFatigue());
            updated = true;
        }
        if (request.getMood() != null) {
            profile.setMood(request.getMood());
            updated = true;
        }
        if (request.getPainLevel() != null) {
            profile.setPainLevel(request.getPainLevel());
            updated = true;
        }
        if (request.getStressLevel() != null) {
            profile.setStressLevel(request.getStressLevel());
            updated = true;
        }


        if (!updated)
            throw new BadRequestException("No fields provided to update");

        repository.save(profile);
    }

    // kafka consumer saving default values :
    public void createDefaultIfNotExists(UUID patientId) {

        log.info("STEP 1: Checking if Symptom Log exists for {}", patientId);
        boolean exists = repository.existsByPatientId(patientId);
        log.info("STEP 2: existsById returned {}", exists);

        if (!exists) {
            log.info("STEP 3: Creating profile");

            SymptomLog profile = new SymptomLog();
            profile.setPatientId(patientId);
            profile.setLogDate(java.time.LocalDate.now());
            repository.save(profile);

            log.info("STEP 4: Symptom Log saved successfully");
        }
    }
}
