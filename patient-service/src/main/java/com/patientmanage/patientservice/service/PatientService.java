package com.patientmanage.patientservice.service;


import com.patientmanage.patientservice.dto.PatientRequestDTO;
import com.patientmanage.patientservice.dto.PatientResponseDTO;
import com.patientmanage.patientservice.exception.EmailAlreadyExistsException;
import com.patientmanage.patientservice.exception.PatientNotFoundException;
import com.patientmanage.patientservice.kafka.KafkaProducer;
import com.patientmanage.patientservice.mapper.PatientMapper;
import com.patientmanage.patientservice.model.Patient;
import com.patientmanage.patientservice.repository.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    private PatientRepository patientRepository;
    private final KafkaProducer kafkaProducer;
    public PatientService(PatientRepository patientRepository, KafkaProducer kafkaProducer){

        this.patientRepository=patientRepository;
        this.kafkaProducer = kafkaProducer;
    }

    public List<PatientResponseDTO> getPatients(){
        List<Patient> patients=patientRepository.findAll();
        List<PatientResponseDTO> patientResponseDTOs= patients.stream().map(patient -> PatientMapper.toDTO(patient)).toList();
        return patientResponseDTOs;
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){

        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())){
            throw new EmailAlreadyExistsException("A patient with this email already exists"+ patientRequestDTO.getEmail());
        }
        Patient newPatient = patientRepository.save(PatientMapper.toPatient(patientRequestDTO));
        kafkaProducer.sendEvent(newPatient);
        return PatientMapper.toDTO(newPatient);
    }


    // update patient
    public PatientResponseDTO updatePatient(UUID id,
                                            PatientRequestDTO patientRequestDTO) {

        Patient patient = patientRepository.findById(id).orElseThrow(
                () -> new PatientNotFoundException("Patient not found with ID: " , id));
        // checks whether there is another email in the database with the same email as the updated one
        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(),
                id)) {
            throw new EmailAlreadyExistsException(
                    "A patient with this email " + "already exists"
                            + patientRequestDTO.getEmail());
        }
        patient.setId(patientRequestDTO.getId());
        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

        Patient updatedPatient = patientRepository.save(patient);
        //kafka producer will send patient
        kafkaProducer.sendEvent(patient);
        return PatientMapper.toDTO(updatedPatient);
    }

    // delete the patient

    public String deletePatient(UUID id) {
        patientRepository.deleteById(id);
        if(patientRepository.existsById(id)){
            return "This record is not deleted !!!";
        }
        return "Record deleted succesfully";
    }


}
