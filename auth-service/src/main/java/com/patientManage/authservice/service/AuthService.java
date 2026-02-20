package com.patientManage.authservice.service;

import com.patientManage.authservice.dto.LoginRequestDTO;
import com.patientManage.authservice.dto.RegisterUserRequestDTO;
import com.patientManage.authservice.model.User;
import com.patientManage.authservice.repository.UserRepository;
import com.patientManage.authservice.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    // authenticate method handles all the business logic
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, UserRepository userRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public Optional<String> authenticate(LoginRequestDTO loginRequestDTO) {
        //1. get the email
        //2. password request -> password -> encoded -> $fnkf\ffe546fefen
        //3.

        Optional<String> token =userService
                .findByEmail(loginRequestDTO.getEmail())
                .filter(u -> passwordEncoder.matches(loginRequestDTO.getPassword(),u.getPassword()))
                .map(u -> jwtUtil.generateToken(u.getEmail(),u.getRole()));

        return token;

    }


    public boolean validateToken(String token) {
        try{
            jwtUtil.validateToken(token);
            return true;
        } catch (JwtException e) {
            return false;
        }

    }
    public UUID registerUser(RegisterUserRequestDTO dto) {

        if(userRepository.existsByEmail(dto.getEmail()))
            throw new RuntimeException("Email already registered");

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());

        userRepository.save(user);

        return user.getId();
    }


}
