package com.patientmanage.patientservice.controller;


import com.patientmanage.patientservice.dto.PatientRequestDTO;
import com.patientmanage.patientservice.dto.PatientResponseDTO;
import com.patientmanage.patientservice.dto.validators.CreatePatientValidationGroup;
import com.patientmanage.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@Tag(name="Patient",description="API for managing Patients")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }
    // getting all patient endpoint
    @GetMapping
    @Operation(summary="Get Patients")
    public ResponseEntity<List<PatientResponseDTO>> getPatient(){
        List<PatientResponseDTO> patients = patientService.getPatients();
        return ResponseEntity.ok().body(patients);
    }

    @PostMapping()
    @Operation(summary="Create patient")
    public ResponseEntity<PatientResponseDTO> createPatient(@Validated({Default.class, CreatePatientValidationGroup.class}) @RequestBody PatientRequestDTO patientRequestDTO){
        PatientResponseDTO patientResponseDTO = patientService.createPatient(patientRequestDTO);
        return ResponseEntity.ok().body(patientResponseDTO);
    }


    //update patient method
    // url : localhost:4000/patients/1233456-123456-123456
    @PutMapping("/{id}")
    @Operation(summary="Update patient")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID id,
                                                            @Validated({Default.class}) @RequestBody PatientRequestDTO patientRequestDTO) {

        PatientResponseDTO patientResponseDTO = patientService.updatePatient(id, patientRequestDTO);

        return ResponseEntity.ok().body(patientResponseDTO);
    }
    @DeleteMapping("/{id}")
    @Operation(summary="Delete patient")
    public String deletePatient(@PathVariable UUID id){
        String line =patientService.deletePatient(id);
        return line;
    }
}
