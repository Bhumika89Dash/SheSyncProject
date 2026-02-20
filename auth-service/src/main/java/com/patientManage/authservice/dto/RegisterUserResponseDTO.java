package com.patientManage.authservice.dto;

import java.util.UUID;

public class RegisterUserResponseDTO {
    private UUID userId;

    public RegisterUserResponseDTO(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }

}
