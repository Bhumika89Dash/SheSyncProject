package com.patientManage.cycle_tracker_service.repository;

import com.patientManage.cycle_tracker_service.model.SymptomLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface SymptomLogRepository extends JpaRepository<SymptomLog, UUID> {

    Optional<SymptomLog> findByPatientId(UUID patientId);

    boolean existsByPatientId(UUID patientId);
}
