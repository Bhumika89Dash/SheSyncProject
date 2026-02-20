package com.patientmanage.patientservice.dto;
import com.patientmanage.patientservice.dto.validators.CreatePatientValidationGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class PatientRequestDTO {
    private UUID id;
    @NotBlank(message="Name is required")
    @Size(max=100,message="Name cannot exceed 100 characters")
    private String name;

    @NotBlank(message="Email is required")
    @Email(message="Email should be valid")
    private String email;

    @NotBlank(message="Address is required")
    private String Address;

    @NotBlank(message="Date of birth is required")
    private String dateOfBirth;


    @NotBlank(groups = CreatePatientValidationGroup.class, message="Registered date is required")
    private String registrationDate;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registeredDate) {
        this.registrationDate = registeredDate;
    }
}
