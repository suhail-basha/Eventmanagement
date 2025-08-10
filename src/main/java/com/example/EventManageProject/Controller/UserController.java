package com.example.EventManageProject.Controller;

import com.example.EventManageProject.Entity.Event;
import com.example.EventManageProject.Entity.EventRegistration;
import com.example.EventManageProject.Entity.Speaker;
import com.example.EventManageProject.Entity.User;
import com.example.EventManageProject.Repositery.UserRepository;
import com.example.EventManageProject.Service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasRole('USER')")
public class UserController {

    @Autowired
    private EventService eventService;
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private SpeakerService speakerService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;

    BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();

    @GetMapping("/profile")
    public String viewProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));


        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }

        model.addAttribute("user", user);
        return "user/profile";
    }

    // ✅ Show Edit Profile Form
    @GetMapping("/profile/edit")
    public String showEditForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }

        model.addAttribute("user", user);
        return "user/edit-profile";
    }

    // ✅ Handle Edit Profile Submission
    @PostMapping("/profile/edit")
    public String updateProfile(@ModelAttribute("user") @Valid User updatedUser,
                                BindingResult result,
                                @AuthenticationPrincipal UserDetails userDetails,
                                Model model) {

        if (result.hasErrors()) {
            return "user/edit-profile";
        }

        // Get currently logged-in user from DB
        String currentEmail = userDetails.getUsername();
        User existingUser = userRepository.findByEmail(currentEmail).orElseThrow(null);
        if (existingUser == null) {
            throw new RuntimeException("User not found with email: " + currentEmail);
        }

        // ✅ Email is NOT updated even if changed in form
        // You may optionally log a warning if the email values differ

        // Update other fields
        existingUser.setName(updatedUser.getName());
        existingUser.setAddress(updatedUser.getAddress());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());

        // Optional password update
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            existingUser.setPassword(encoder.encode(updatedUser.getPassword()));
        }

        // Save updated user
        userRepository.save(existingUser);

        return "redirect:/user/profile?success";
    }


    //  SHOW ALL EVENTS WITH FILTERS
    @GetMapping("/events")
    public String showEvents(@RequestParam(required = false) String location,
                             @RequestParam(required = false) String category,
                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                             Model model) {

        List<Event> events = eventService.searchEvents(location, category, date != null ? date.atStartOfDay() : null);
        List<String> categories = eventService.getAllCategories();

        model.addAttribute("events", events);
        model.addAttribute("location", location);
        model.addAttribute("category", category);
        model.addAttribute("date", date);
        model.addAttribute("categories", categories);

        return "user/index";
    }

    //  VIEW EVENT DETAILS
    @GetMapping("/event/{id}")
    public String viewEventDetails(@PathVariable Long id, Model model, Principal principal) {
        Event event = eventService.getEventEntityById(id);
        if (event == null) {
            return "redirect:/user/events?error=eventNotFound";
        }

        User user = userService.getUserByEmail(principal.getName());

        boolean alreadyRegistered = registrationService.isUserRegistered(user.getId(), event.getId());
        boolean hasAttended = attendanceService.hasUserAttended(user, event);
        List<Speaker> speakers = speakerService.getSpeakersByEvent(event);

        model.addAttribute("event", event);
        model.addAttribute("alreadyRegistered", alreadyRegistered);
        model.addAttribute("hasAttended", hasAttended);
        model.addAttribute("speakers", speakers);

        return "user/event-details";
    }

    //  REGISTER FOR AN EVENT
    @PostMapping("/register/{eventId}")
    public String registerForEvent(@PathVariable Long eventId,
                                   @AuthenticationPrincipal UserDetails userDetails) {

        Event event = eventService.getEventEntityById(eventId);
        if (event == null) {
            return "redirect:/user/events?error=eventNotFound";
        }

        User user = userService.getUserByEmail(userDetails.getUsername());
        EventRegistration registration = registrationService.findByUserIdAndEventId(user.getId(), eventId);

        if (registration != null) {
            // Update existing registration (optional logic)
            registration.setRegisteredAt(LocalDateTime.now());
            registrationService.saveRegistration(registration);
        } else {
            // Create new registration
            registration = new EventRegistration();
            registration.setEvent(event);
            registration.setUser(user);
            registration.setRegisteredAt(LocalDateTime.now());
            registration.setAttended(null); // Default to false
            registrationService.saveRegistration(registration);
        }

        emailService.sendRegistrationConfirmation(user.getEmail(), event.getTitle());

        return "redirect:/user/my-registrations";
    }

    //  VIEW MY REGISTRATIONS
    @GetMapping("/my-registrations")
    public String viewMyRegistrations(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        List<EventRegistration> registrations = registrationService.getRegistrationsByUser(user);
        model.addAttribute("registrations", registrations);
        return "user/my-registrations";
    }
}
