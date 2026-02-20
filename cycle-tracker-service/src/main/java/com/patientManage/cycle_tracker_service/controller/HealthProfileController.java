package com.patientManage.cycle_tracker_service.controller;

import com.patientManage.cycle_tracker_service.dto.HealthProfileRequest;
import com.patientManage.cycle_tracker_service.model.HealthProfile;
import com.patientManage.cycle_tracker_service.service.HealthProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/health-profile")
public class HealthProfileController {

    private final HealthProfileService service;

    public HealthProfileController(HealthProfileService service) {
        this.service = service;
    }

    @PatchMapping
    public ResponseEntity<?> updateProfile(
            @RequestHeader("X-USER-ID") UUID patientId,
            @RequestBody HealthProfileRequest request) {

        service.update(patientId, request);
        return ResponseEntity.ok("Profile updated successfully");
    }

    @GetMapping
    public ResponseEntity<HealthProfile> getProfile(
            @RequestHeader("X-USER-ID") UUID userId) {

        return ResponseEntity.ok(service.get(userId));
    }
}

