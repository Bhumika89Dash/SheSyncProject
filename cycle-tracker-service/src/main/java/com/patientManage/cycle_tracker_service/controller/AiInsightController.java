package com.patientManage.cycle_tracker_service.controller;

import com.patientManage.cycle_tracker_service.dto.AiInsightRequest;
import com.patientManage.cycle_tracker_service.dto.AiInsightResponse;
import com.patientManage.cycle_tracker_service.service.AiInsightService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("ai/insights")
@CrossOrigin(origins = "*")
public class AiInsightController {

    private final AiInsightService aiInsightService;

    public AiInsightController(AiInsightService aiInsightService) {
        this.aiInsightService = aiInsightService;
    }

    @PostMapping("/generate/{userId}")
    public Mono<AiInsightResponse> generateInsight(
            @PathVariable UUID userId) {

        return aiInsightService.generateInsight(userId)
                .map(AiInsightResponse::new)
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.error(new RuntimeException("AI ERROR: " + e.getMessage()));
                });
    }
}
