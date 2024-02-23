package com.grofers.service;

import com.grofers.dto.UserDTO;
import com.grofers.exceptions.NotFoundException;
import com.grofers.model.User;
import com.grofers.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
@Service
public class UserServiceImpl implements  UserService{
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public UserDTO register(User user) throws NotFoundException {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new NotFoundException("user already exist!!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return UserDTO.builder()
                .email(user.getEmail())
                .name(user.getFullName())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
