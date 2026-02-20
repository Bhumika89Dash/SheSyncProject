package com.patientManage.cycle_tracker_service.dto;

import java.time.LocalDate;

public class SymptomLogRequest {
    private String mood;
    private Integer painLevel;
    private Boolean fatigue;
    private Boolean bloating;
    private Boolean cramps;
    private Integer stressLevel;

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
