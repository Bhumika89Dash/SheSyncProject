package com.patientmanage.patientservice.mapper;

import com.patientmanage.patientservice.dto.PatientRequestDTO;
import com.patientmanage.patientservice.dto.PatientResponseDTO;
import com.patientmanage.patientservice.model.Patient;

import java.time.LocalDate;

public class PatientMapper {
    public static PatientResponseDTO toDTO(Patient patient){
        PatientResponseDTO patientDTO = new PatientResponseDTO();
        patientDTO.setId(patient.getId().toString());
        patientDTO.setName(patient.getName());
        patientDTO.setAddress(patient.getAddress());
        patientDTO.setEmail(patient.getEmail());
        patientDTO.setDateOfBirth((patient.getDateOfBirth()).toString());
        return patientDTO;
    }
    public static Patient toPatient(PatientRequestDTO patientRequestDTO){
        Patient patient=new Patient();
        patient.setId(patientRequestDTO.getId());
        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        patient.setRegistrationDate(LocalDate.parse(patientRequestDTO.getRegistrationDate()));
        return patient;


    }

}


