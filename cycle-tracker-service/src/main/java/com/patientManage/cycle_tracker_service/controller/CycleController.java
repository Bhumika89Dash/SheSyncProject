package com.patientManage.cycle_tracker_service.controller;


import com.patientManage.cycle_tracker_service.dto.CycleHistoryResponse;
import com.patientManage.cycle_tracker_service.dto.CycleLogRequest;
import com.patientManage.cycle_tracker_service.dto.CycleLogResponse;
import com.patientManage.cycle_tracker_service.model.CycleLog;
import com.patientManage.cycle_tracker_service.service.CycleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/cycles")
public class CycleController {

    private final CycleService service;

    public CycleController(CycleService service) {
        this.service = service;
    }

    // onboarding/manual entry
    @PostMapping
    public ResponseEntity<Void> addCycle(
            @RequestHeader("X-USER-ID") UUID patientId,
            @RequestBody CycleLogRequest request) {

        service.saveCycle(patientId, request);
        return ResponseEntity.ok().build();
    }

    // start period button
    @PostMapping("/start")
    public ResponseEntity<Void> startCycle(
            @RequestHeader("X-USER-ID") UUID patientId,
            @RequestBody CycleLogRequest request) {

        service.startCycle(patientId, request.getStartDate());
        return ResponseEntity.ok().build();
    }

    // end period button
    @PatchMapping("/end")
    public ResponseEntity<Void> endCycle(
            @RequestHeader("X-USER-ID") UUID patientId,
            @RequestBody CycleLogRequest request) {

        service.endCycle(patientId, request.getEndDate());
        return ResponseEntity.ok().build();
    }

    // dashboard
    @GetMapping("/prediction")
    public ResponseEntity<CycleLogResponse> getPrediction(
            @RequestHeader("X-USER-ID") UUID patientId) {

        return ResponseEntity.ok(service.predict(patientId));
    }

    @GetMapping("/history")
    public ResponseEntity<List<CycleHistoryResponse>> history(
            @RequestHeader("X-USER-ID") UUID patientId) {

        return ResponseEntity.ok(service.history(patientId));
    }
}
