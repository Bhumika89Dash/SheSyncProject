package com.patientManage.cycle_tracker_service.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "health_profile")
public class HealthProfile {

    @Id
    @Column(name = "user_id")
    private UUID patientId;

    @Column(name = "known_pcos")
    private Boolean knownPcos;

    private Boolean thyroid;
    private Boolean diabetes;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
