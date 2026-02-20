package com.patientManage.authservice.service;

import com.patientManage.authservice.model.User;
import com.patientManage.authservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepo;
    public UserService(UserRepository userRepo) {
        this.userRepo= userRepo;
    }
    public Optional<User> findByEmail(String email) {
        // logic to find the user by email
        return userRepo.findByEmail(email);
    }

}
