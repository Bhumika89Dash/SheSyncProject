package com.patientManage.cycle_tracker_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patientManage.cycle_tracker_service.dto.AiInsightRequest;
import com.patientManage.cycle_tracker_service.model.HealthProfile;
import com.patientManage.cycle_tracker_service.model.SymptomLog;
import com.patientManage.cycle_tracker_service.repository.HealthProfileRepository;
import com.patientManage.cycle_tracker_service.repository.SymptomLogRepository;
import com.patientManage.cycle_tracker_service.util.PromptBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@Service
public class AiInsightService {

    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final HealthProfileRepository healthProfileRepo;
    private final SymptomLogRepository symptomLogRepo;

    public AiInsightService(WebClient.Builder webClientBuilder, HealthProfileRepository healthProfileRepo, SymptomLogRepository symptomLogRepo) {
        this.webClient = webClientBuilder.build();
        this.healthProfileRepo = healthProfileRepo;
        this.symptomLogRepo = symptomLogRepo;
    }
    public Mono<String> generateInsight(UUID userId) {

        HealthProfile profile = healthProfileRepo.findByPatientId(userId)
                .orElseThrow(() -> new RuntimeException("Health profile not found"));

        SymptomLog symptom = symptomLogRepo.findByPatientId(userId)
                .orElseThrow(() -> new RuntimeException("Symptoms not found"));


        AiInsightRequest request = mapToRequest(userId, profile, symptom);

        return callGemini(request);
    }
    public Mono<String> callGemini(AiInsightRequest request) {
        String prompt = PromptBuilder.buildPrompt(request);

        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of(
                                "parts", new Object[]{
                                        Map.of("text", prompt)
                                }
                        )
                },
                // Optional: You can add safety settings, generation config, etc.
                "generationConfig", Map.of(
                        "temperature", 0.7,
                        "maxOutputTokens", 2048
                )
        );

        return webClient.post()
                .uri(geminiApiUrl)
                .header("Content-Type", "application/json")
                .header("x-goog-api-key", geminiApiKey)   // ← correct header name (lowercase x)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(60))           // generous client-side timeout
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
                        .filter(throwable ->
                                throwable instanceof WebClientRequestException ||
                                        throwable.getMessage() != null && (
                                                throwable.getMessage().contains("Connection reset") ||
                                                        throwable.getMessage().contains("timeout") ||
                                                        throwable.getMessage().contains("Unable to acquire")
                                        )
                        )
                        .doBeforeRetry(signal ->
                                System.err.println(
                                        "Retry attempt #" + (signal.totalRetries() + 1) +
                                                " after: " + signal.failure()
                                )
                        )
                )
                .map(this::extractText)
                .onErrorResume(throwable -> {
                    System.err.println("Gemini API call failed: " + throwable.getMessage());
                    return Mono.just("Sorry, something went wrong on our side. Please try again later 💙");
                });
    }
    private AiInsightRequest mapToRequest(UUID userId,
                                          HealthProfile profile,
                                          SymptomLog symptom) {

        return AiInsightRequest.builder()
                .userId(userId)
                .knownPcos(profile.getKnownPcos())
                .thyroid(profile.getThyroid())
                .diabetes(profile.getDiabetes())
                .mood(symptom.getMood())
                .painLevel(symptom.getPainLevel())
                .fatigue(symptom.getFatigue())
                .bloating(symptom.getBloating())
                .cramps(symptom.getCramps())
                .stressLevel(symptom.getStressLevel())
                .build();
    }
    private String extractText(String response) {
        if (response == null || !response.trim().startsWith("{")) {
            return response != null ? response : "No response from AI";
        }

        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(response);

            com.fasterxml.jackson.databind.JsonNode candidates = root.path("candidates");
            if (candidates.isEmpty()) {
                JsonNode error = root.path("error");
                if (!error.isMissingNode()) {
                    return "API Error: " + error.path("message").asText("Unknown error");
                }
                return "No candidates returned 💙";
            }

            return candidates.get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText("You're doing great — take it one step at a time 💙");

        } catch (Exception e) {
            System.err.println("Failed to parse Gemini response: " + e.getMessage());
            return "Error processing response: " + e.getMessage();
        }
    }
}