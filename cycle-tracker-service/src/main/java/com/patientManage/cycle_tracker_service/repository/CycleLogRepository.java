package com.patientManage.cycle_tracker_service.repository;

import com.patientManage.cycle_tracker_service.model.CycleLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CycleLogRepository extends JpaRepository<CycleLog, UUID> {
    List<CycleLog> findByPatientIdOrderByStartDateDesc(UUID patientId);
    Optional<CycleLog> findTopByPatientIdOrderByStartDateDesc(UUID patientId);

    boolean existsByPatientIdAndEndDateIsNull(UUID patientId);
}