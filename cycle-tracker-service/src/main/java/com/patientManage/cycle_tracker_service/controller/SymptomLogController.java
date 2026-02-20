package com.patientManage.cycle_tracker_service.controller;

import com.patientManage.cycle_tracker_service.dto.SymptomLogRequest;
import com.patientManage.cycle_tracker_service.model.SymptomLog;
import com.patientManage.cycle_tracker_service.service.SymptomLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/symptoms")
public class SymptomLogController {

    private final SymptomLogService service;

    public SymptomLogController(SymptomLogService service) {
        this.service = service;
    }

    @PatchMapping
    public ResponseEntity<Void> addLog(
            @RequestHeader("X-USER-ID") UUID userId,
            @RequestBody SymptomLogRequest request) {

        service.update(userId, request);
        return ResponseEntity.ok().build();
    }
}

