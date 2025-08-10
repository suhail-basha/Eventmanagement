package com.example.EventManageProject.Controller;

import com.example.EventManageProject.Entity.*;
import com.example.EventManageProject.Repositery.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import java.util.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDetails userDetails;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private UserController userController;

    private User testUser;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1L);
        testUser.setName("John Doe");
        testUser.setEmail("john@example.com");
        testUser.setPhoneNumber("1234567890");
        testUser.setAddress("123 Street");
        testUser.setPassword("encryptedPassword");
    }

    @Test
    void testViewProfile() {
        when(userDetails.getUsername()).thenReturn("john@example.com");
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(testUser));

        String viewName = userController.viewProfile(userDetails, model);

        assertEquals("user/profile", viewName);
        verify(model).addAttribute("user", testUser);
    }

    @Test
    void testShowEditForm() {
        when(userDetails.getUsername()).thenReturn("john@example.com");
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(testUser));

        String viewName = userController.showEditForm(userDetails, model);

        assertEquals("user/edit-profile", viewName);
        verify(model).addAttribute("user", testUser);
    }

    @Test
    void testUpdateProfile_success() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userDetails.getUsername()).thenReturn("john@example.com");
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any())).thenReturn(testUser);

        User updatedUser = new User();
        updatedUser.setName("Updated Name");
        updatedUser.setAddress("Updated Address");
        updatedUser.setPhoneNumber("9876543210");
        updatedUser.setPassword("pass123");

        String result = userController.updateProfile(updatedUser, bindingResult, userDetails, model);

        assertEquals("redirect:/user/profile?success", result);

        // Verify that password was encoded and set
        verify(userRepository).save(argThat(user ->
                user.getName().equals("Updated Name") &&
                        user.getAddress().equals("Updated Address") &&
                        user.getPhoneNumber().equals("9876543210") &&
                        user.getPassword() != null && !user.getPassword().isBlank()
        ));
    }

}
