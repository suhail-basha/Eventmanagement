package com.example.EventManageProject.Service;

import com.example.EventManageProject.Entity.Role;
import com.example.EventManageProject.Entity.User;
import com.example.EventManageProject.Repositery.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.EventManageProject.Dto.UserDto;
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();

    public long countUsers() {
        return userRepository.count();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }


    public void registerUser(UserDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        user.setRole(Role.USER);
        user.setEnabled(true);
        userRepository.save(user);
    }
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }
}
