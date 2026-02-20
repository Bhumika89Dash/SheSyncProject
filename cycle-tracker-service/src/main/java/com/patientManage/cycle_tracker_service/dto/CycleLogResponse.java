package com.patientManage.cycle_tracker_service.dto;

import java.time.LocalDate;

public class CycleLogResponse {
    private LocalDate nextPeriodDate;
    private long daysLeft;
    private String phase;
    private String statusMessage;

    public LocalDate getNextPeriodDate() {
        return nextPeriodDate;
    }

    public void setNextPeriodDate(LocalDate nextPeriodDate) {
        this.nextPeriodDate = nextPeriodDate;
    }

    public long getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(long daysLeft) {
        this.daysLeft = daysLeft;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
