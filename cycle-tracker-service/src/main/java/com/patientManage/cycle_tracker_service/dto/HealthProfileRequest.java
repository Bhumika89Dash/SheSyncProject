package com.patientManage.cycle_tracker_service.dto;

public class HealthProfileRequest {
    private Boolean knownPcos;
    private Boolean thyroid;
    private Boolean diabetes;

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
}
