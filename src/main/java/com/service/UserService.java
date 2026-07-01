package com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.model.User;
import com.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User addUser(User user) {
        user.setRole("ROLE_NORMAL");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User getUserById(int id) {
        return userRepo.findById(id).orElse(null);
    }

    public void deleteUser(int id) {
        userRepo.deleteById(id);
    }

    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElse(null);
    }

    public User getUserByEmailAndPhone(String email, String phone) {
        return userRepo.findByEmailAndPhone(email, phone).orElse(null);
    }

    public boolean resetPassword(String email, String phone, String newPassword) {

        User user = userRepo.findByEmailAndPhone(email, phone).orElse(null);

        if (user == null) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
        return true;
    }
}