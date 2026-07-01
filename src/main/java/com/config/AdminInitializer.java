package com.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.model.User;
import com.repository.UserRepository;

@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        Optional<User> existingAdmin = userRepo.findByEmail("admin");

        if (existingAdmin.isEmpty()) {

            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setPhone("0000000000");
            admin.setStatus("ACTIVE");
            admin.setRole("ROLE_ADMIN");

            userRepo.save(admin);

            System.out.println("✅ ADMIN CREATED");
        } else {
            System.out.println("ℹ️ ADMIN ALREADY EXISTS");
        }
    }
}