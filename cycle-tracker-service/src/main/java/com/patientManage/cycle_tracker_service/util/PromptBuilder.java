package com.patientManage.cycle_tracker_service.util;

import com.patientManage.cycle_tracker_service.dto.AiInsightRequest;

public class PromptBuilder {

    public static String buildPrompt(AiInsightRequest request) {

        StringBuilder prompt = new StringBuilder();

        prompt.append("""
            You are a compassionate women's health assistant for a period tracking app called SheSync.
            Your tone should be calm, reassuring, positive, and non-alarming.
            Do give medical diagnosis.
            Do suggest medication.
            Keep the response short (10-15 lines).
            give her some checklist in points of simple advices... (like drink water, exercise etc)...
            """)

                .append("\nUser health context:\n");

        if (Boolean.TRUE.equals(request.getKnownPcos())) {
            prompt.append("- User has PCOS\n");
        }
        if (Boolean.TRUE.equals(request.getThyroid())) {
            prompt.append("- User has thyroid issues\n");
        }
        if (Boolean.TRUE.equals(request.getDiabetes())) {
            prompt.append("- User has diabetes\n");
        }

        prompt.append("\nCurrent symptoms:\n");

        if (request.getMood() != null) {
            prompt.append("- Mood: ").append(request.getMood()).append("\n");
        }
        if (request.getPainLevel() != null) {
            prompt.append("- Pain level (0–10): ").append(request.getPainLevel()).append("\n");
        }
        if (Boolean.TRUE.equals(request.getFatigue())) {
            prompt.append("- Fatigue present\n");
        }
        if (Boolean.TRUE.equals(request.getBloating())) {
            prompt.append("- Bloating present\n");
        }
        if (Boolean.TRUE.equals(request.getCramps())) {
            prompt.append("- Cramps present\n");
        }
        if (request.getStressLevel() != null) {
            prompt.append("- Stress level (0–10): ").append(request.getStressLevel()).append("\n");
        }

        prompt.append("""
            \nGenerate a supportive and empathetic message for the user.
            Include emotional reassurance and gentle self-care suggestions.
            End with a positive note.
            """);

        return prompt.toString();
    }
}