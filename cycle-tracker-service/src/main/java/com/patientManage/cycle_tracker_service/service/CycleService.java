package com.patientManage.cycle_tracker_service.service;

import com.patientManage.cycle_tracker_service.dto.CycleHistoryResponse;
import com.patientManage.cycle_tracker_service.dto.CycleLogRequest;
import com.patientManage.cycle_tracker_service.dto.CycleLogResponse;
import com.patientManage.cycle_tracker_service.exception.BadRequestException;
import com.patientManage.cycle_tracker_service.exception.NotFoundException;
import com.patientManage.cycle_tracker_service.model.CycleLog;
import com.patientManage.cycle_tracker_service.repository.CycleLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CycleService {
    private final CycleLogRepository repository;

    public CycleService(CycleLogRepository repository) {
        this.repository = repository;
    }

    // Save cycle (onboarding or normal logging)
    public void saveCycle(UUID patientId, CycleLogRequest request) {
        CycleLog log = new CycleLog();
        log.setId(UUID.randomUUID());   // REQUIRED
        log.setPatientId(patientId);
        log.setStartDate(request.getStartDate());
        log.setEndDate(request.getEndDate());
        log.setCreatedAt(LocalDateTime.now());

        repository.save(log);
    }

    // Predict next cycle
    public CycleLogResponse predict(UUID patientId) {
        List<CycleLog> logs = repository.findByPatientIdOrderByStartDateDesc(patientId);

        if (logs.isEmpty()) return null; // frontend will ask user to add data

        CycleLog last = logs.get(0);
        int avgCycleLength = calculateAverageCycle(logs);

        LocalDate expectedDate = last.getStartDate().plusDays(avgCycleLength);
        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), expectedDate);

        CycleLogResponse response = new CycleLogResponse();
        response.setNextPeriodDate(expectedDate);
        response.setDaysLeft(daysLeft);
        response.setPhase(calculatePhase(last.getStartDate()));
        response.setStatusMessage(getStatusMessage(daysLeft));

        return response;
    }


    // Average cycle calculation
    private int calculateAverageCycle(List<CycleLog> logs) {
        if (logs.size() < 2) return 28; // fallback
        List<Long> gaps = new ArrayList<>();
        for (int i = 0; i < logs.size() - 1; i++) {
            long diff = ChronoUnit.DAYS.between(logs.get(i + 1).getStartDate(), logs.get(i).getStartDate());
            gaps.add(diff);
        }
        return (int) gaps.stream().mapToLong(Long::longValue).average().orElse(28);
    }

    // Phase calculation
    private String calculatePhase(LocalDate startDate) {
        int day = (int) ChronoUnit.DAYS.between(startDate, LocalDate.now()) + 1;
        if (day <= 5) return "MENSTRUAL";
        if (day <= 13) return "FOLLICULAR";
        if (day == 14) return "OVULATION";
        return "LUTEAL";
    }

    // Positive status message
    private String getStatusMessage(long daysLeft) {
        if (daysLeft > 0) return "Your period is on time 🌸";
        else if (daysLeft == 0) return "Your period is expected today 💖";
        else if (daysLeft >= -5) return "Your period is slightly delayed 🌼";
        else return "Your period is delayed. Please take care 💙";
    }

    public void startCycle(UUID patientId, LocalDate startDate) {

        if (repository.existsByPatientIdAndEndDateIsNull(patientId))
            throw new BadRequestException("Cycle already active");

        CycleLog log = new CycleLog();
        log.setId(UUID.randomUUID());
        log.setPatientId(patientId);
        log.setStartDate(startDate);

        repository.save(log);
    }

    public void endCycle(UUID patientId, LocalDate endDate) {

        CycleLog log = repository
                .findTopByPatientIdOrderByStartDateDesc(patientId)
                .orElseThrow(() -> new NotFoundException("No cycle found"));

        if (log.getEndDate() != null)
            throw new BadRequestException("No active cycle");

        if (endDate.isBefore(log.getStartDate()))
            throw new BadRequestException("End date before start date");

        log.setEndDate(endDate);
        repository.save(log);
    }

    public List<CycleHistoryResponse> history(UUID patientId) {

        List<CycleLog> logs = repository.findByPatientIdOrderByStartDateDesc(patientId);

        return logs.stream().map(log -> {
            CycleHistoryResponse dto = new CycleHistoryResponse();

            dto.setStartDate(log.getStartDate());
            dto.setEndDate(log.getEndDate());

            if (log.getEndDate() != null) {
                dto.setPeriodLength(
                        (int) ChronoUnit.DAYS.between(log.getStartDate(), log.getEndDate()) + 1
                );
                dto.setOngoing(false);
            } else {
                dto.setPeriodLength(
                        (int) ChronoUnit.DAYS.between(log.getStartDate(), LocalDate.now()) + 1
                );
                dto.setOngoing(true);
            }

            return dto;
        }).toList();
    }





}

