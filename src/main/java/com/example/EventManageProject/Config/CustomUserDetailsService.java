package com.example.EventManageProject.Config;

import com.example.EventManageProject.Entity.User;
import com.example.EventManageProject.Repositery.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // Hardcoded admin details
    private static final String ADMIN_EMAIL = "admin@gmail.com";
    private static final String ADMIN_PASSWORD = "$2y$10$B1nr7G3A.O1VJVjLo4t9FuPRbsh2hDMdE//bmaQvefgKHLRWWoc4G";

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // If admin login
        if (ADMIN_EMAIL.equals(email)) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(ADMIN_EMAIL)
                    .password(ADMIN_PASSWORD)
                    .roles("ADMIN")
                    .build();
        }

        // Regular user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));;
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name()) // Assuming ENUM like Role.USER
                .build();
    }
}
