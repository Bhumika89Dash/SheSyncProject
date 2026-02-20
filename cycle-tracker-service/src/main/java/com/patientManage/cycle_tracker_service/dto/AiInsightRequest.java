package com.patientManage.cycle_tracker_service.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class AiInsightRequest {

    private UUID userId;

    // Health Profile
    private Boolean knownPcos;
    private Boolean thyroid;
    private Boolean diabetes;
    private String mood;
    private Integer painLevel;
    private Boolean fatigue;
    private Boolean bloating;
    private Boolean cramps;
    private Integer stressLevel;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Boolean getKnownPcos() {
        return knownPcos;
    }

    public void setKnownPcos(Boolean knownPcos) {
        this.knownPcos = knownPcos;
    }

    public Boolean getThyroid() {
        return thyroid;
    }

    public void setThyroid(Boolean thyroid) {
        this.thyroid = thyroid;
    }

    public Boolean getDiabetes() {
        return diabetes;
    }

    public void setDiabetes(Boolean diabetes) {
        this.diabetes = diabetes;
    }



    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public Integer getPainLevel() {
        return painLevel;
    }

    public void setPainLevel(Integer painLevel) {
        this.painLevel = painLevel;
    }

    public Boolean getFatigue() {
        return fatigue;
    }

    public void setFatigue(Boolean fatigue) {
        this.fatigue = fatigue;
    }

    public Boolean getBloating() {
        return bloating;
    }

    public void setBloating(Boolean bloating) {
        this.bloating = bloating;
    }

    public Boolean getCramps() {
        return cramps;
    }

    public void setCramps(Boolean cramps) {
        this.cramps = cramps;
    }

    public Integer getStressLevel() {
        return stressLevel;
    }

    public void setStressLevel(Integer stressLevel) {
        this.stressLevel = stressLevel;
    }
}
