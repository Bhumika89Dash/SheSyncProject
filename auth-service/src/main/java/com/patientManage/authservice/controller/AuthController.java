package com.patientManage.authservice.controller;


import com.patientManage.authservice.dto.LoginRequestDTO;
import com.patientManage.authservice.dto.LoginResponseDTO;
import com.patientManage.authservice.dto.RegisterUserRequestDTO;
import com.patientManage.authservice.dto.RegisterUserResponseDTO;
import com.patientManage.authservice.service.AuthService;
import com.patientManage.authservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Generate token on user login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        Optional<String> tokenOptional = authService.authenticate(loginRequestDTO);
        if (tokenOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        }
        String token = tokenOptional.get();
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
    @Operation(summary = "Validate Token")
    @GetMapping("/validate")
    public ResponseEntity<Void> validateToken(@RequestHeader("Authorization") String authHeader) {
        //header Authorization: Bearer<token>
        if(authHeader== null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return authService.validateToken(authHeader.substring(7))
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // create new user

    @PostMapping("/users")
    public ResponseEntity<RegisterUserResponseDTO> registerUser(
            @RequestBody RegisterUserRequestDTO dto) {

        UUID userId = authService.registerUser(dto);
        return ResponseEntity.ok(new RegisterUserResponseDTO(userId));
    }

}
