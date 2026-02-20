package com.patientManage.cycle_tracker_service.service;

import com.patientManage.cycle_tracker_service.dto.HealthProfileRequest;
import com.patientManage.cycle_tracker_service.exception.BadRequestException;
import com.patientManage.cycle_tracker_service.exception.NotFoundException;
import com.patientManage.cycle_tracker_service.model.HealthProfile;
import com.patientManage.cycle_tracker_service.repository.HealthProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class HealthProfileService {

    private static final Logger log = LoggerFactory.getLogger(HealthProfileService.class);

    private final HealthProfileRepository repository;

    public HealthProfileService(HealthProfileRepository repository) {
        this.repository = repository;
    }

    public void update(UUID userId, HealthProfileRequest request) {

        if (request == null)
            throw new BadRequestException("Request body missing");

        HealthProfile profile = repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Health profile not created yet"));

        boolean updated = false;

        if (request.getKnownPcos() != null) {
            profile.setKnownPcos(request.getKnownPcos());
            updated = true;
        }

        if (request.getThyroid() != null) {
            profile.setThyroid(request.getThyroid());
            updated = true;
        }

        if (request.getDiabetes() != null) {
            profile.setDiabetes(request.getDiabetes());
            updated = true;
        }

        if (!updated)
            throw new BadRequestException("No fields provided to update");

        repository.save(profile);
    }

    public HealthProfile get(UUID userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    // automatic creation of data row when user registers
   /* public void createDefaultIfNotExists(UUID userId) {

        boolean exists = repository.existsById(userId);

        if (!exists) {
            HealthProfile profile = new HealthProfile();
            profile.setPatientId(userId);

            // default values (unknown yet)
            profile.setKnownPcos(null);
            profile.setThyroid(null);
            profile.setDiabetes(null);

            repository.save(profile);
        }
    }*/
    public void createDefaultIfNotExists(UUID userId) {

        log.info("STEP 1: Checking if Health profile exists for {}", userId);
        boolean exists = repository.existsById(userId);
        log.info("STEP 2: existsById returned {}", exists);

        if (!exists) {
            log.info("STEP 3: Creating profile");

            HealthProfile profile = new HealthProfile();
            profile.setPatientId(userId);

            repository.save(profile);

            log.info("STEP 4: Health Profile saved successfully");
        }
    }


}
