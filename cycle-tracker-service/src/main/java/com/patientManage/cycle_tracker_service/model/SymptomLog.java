package com.patientManage.cycle_tracker_service.model;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

    @Entity
    @Table(name = "symptom_log")
    public class SymptomLog {

        @Id
        @Column(name = "user_id", nullable = false)
        private UUID patientId;

        @Column(name = "log_date", nullable = false)
        private LocalDate logDate;

        private String mood;

        @Column(name = "pain_level")
        private Integer painLevel;

        private Boolean fatigue;
        private Boolean bloating;
        private Boolean cramps;

        @Column(name = "stress_level")
        private Integer stressLevel;

        @Column(name = "created_at")
        private LocalDateTime createdAt = LocalDateTime.now();

        public UUID getPatientId() {
            return patientId;
        }

        public void setPatientId(UUID patientId) {
            this.patientId = patientId;
        }

        public LocalDate getLogDate() {
            return logDate;
        }

        public void setLogDate(LocalDate logDate) {
            this.logDate = logDate;
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

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }
    }


