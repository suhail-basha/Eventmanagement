package com.example.EventManageProject.Controller;

import com.example.EventManageProject.Entity.Event;
import com.example.EventManageProject.Entity.EventRegistration;
import com.example.EventManageProject.Entity.Speaker;
import com.example.EventManageProject.Entity.User;
import com.example.EventManageProject.Repositery.EventRegistrationRepository;
import com.example.EventManageProject.Repositery.EventRepository;
import com.example.EventManageProject.Repositery.SpeakerRepository;
import com.example.EventManageProject.Repositery.UserRepository;
import com.example.EventManageProject.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminEventController {

    private final EventService eventService;
    private final SpeakerService speakerService;
    private final UserService userService;
    private final RegistrationService registrationService;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventRegistrationRepository eventRegistrationRepository;
    private final AttendanceService attendanceService;
    private final SpeakerRepository speakerRepository;

    // Dashboard view with event, user, and attendance statistics
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalEvents", eventService.countEvents());
        model.addAttribute("totalUsers", userService.countUsers());
        model.addAttribute("upcomingEvents", eventService.countUpcomingEvents());
        model.addAttribute("totalAttendances", attendanceService.countAttendances());
        model.addAttribute("attendanceRate", attendanceService.calculateAttendanceRate());
        model.addAttribute("recentRegistrations", registrationService.getRecentRegistrations());
        model.addAttribute("recentAttendances", attendanceService.getRecentAttendances());
        return "admin/dashboard";
    }

    // List all events
    @GetMapping("/events")
    public String showAllEvents(Model model) {
        model.addAttribute("events", eventService.getAllEvents());
        return "admin/events";
    }

    // Show event creation form
    @GetMapping("/add-event")
    public String showAddForm(Model model) {
        model.addAttribute("event", new Event());
        return "admin/add-event";
    }

    // Handle event creation
    @PostMapping("/add-event")
    public String addEvent(@ModelAttribute Event event,
                           @RequestParam("imageFile") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            event.setImage(file.getBytes());
        }
        eventRepository.save(event);
        return "redirect:/admin/events";
    }

    // Show event edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Event> event = eventRepository.findById(id);
        if (event.isPresent()) {
            model.addAttribute("event", event.get());
            return "admin/edit-event";
        }
        return "redirect:/admin/events";
    }

    // Handle event update
    @PostMapping("/update")
    public String updateEvent(@ModelAttribute("event") Event formEvent,
                              @RequestParam("startTime") String startTimeStr,
                              @RequestParam("endTime") String endTimeStr,
                              @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        Event existing = eventRepository.findById(formEvent.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid event ID"));

        existing.setTitle(formEvent.getTitle());
        existing.setDescription(formEvent.getDescription());
        existing.setCategory(formEvent.getCategory());
        existing.setVenue(formEvent.getVenue());
        existing.setStartTime(LocalDateTime.parse(startTimeStr));
        existing.setEndTime(LocalDateTime.parse(endTimeStr));

        if (!imageFile.isEmpty()) {
            existing.setImage(imageFile.getBytes());
        }

        eventRepository.save(existing);
        return "redirect:/admin/events";
    }

    // Delete event
    @GetMapping("/delete-event/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventRepository.deleteById(id);
        return "redirect:/admin/events";
    }

    // User management
    @GetMapping("/users")
    public String viewAllUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin/users"; // Refers to admin/users.html
    }

    @GetMapping("/speakers")
    public String viewSpeakers(Model model) {
        List<Speaker> speakers = speakerService.getAllSpeakers();
        model.addAttribute("speakers", speakers);
        return "admin/manage-speakers";
    }
    @GetMapping("/event/{id}/speakers")
    public String showAddSpeakerForm(@PathVariable("id") Long eventId, Model model) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        if (eventOpt.isEmpty()) {
            return "redirect:/admin/events";
        }

        Speaker speaker = new Speaker();
        model.addAttribute("event", eventOpt.get());
        model.addAttribute("speaker", speaker);

        return "admin/add-speaker";
    }
    @PostMapping("/event/{id}/speakers")
    public String saveSpeakerToEvent(@PathVariable Long id,
                                     @ModelAttribute("speaker") Speaker speaker,
                                     Model model) {
        Optional<Event> eventOpt = eventRepository.findById(id);
        if (eventOpt.isEmpty()) {
            return "redirect:/admin/speakers";
        }

        speaker.setId(null); // 💥 Force Hibernate to treat it as new
        speaker.setEvent(eventOpt.get());
        speakerRepository.save(speaker);

        return "redirect:/admin/speakers";
    }
    @GetMapping("/speaker/edit/{id}")
    public String editSpeaker(@PathVariable Long id, Model model) {
        Speaker speaker = speakerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid speaker ID: " + id));
        model.addAttribute("speaker", speaker);
        model.addAttribute("events", eventRepository.findAll());
        return "admin/edit-speaker";
    }

    @PostMapping("/speaker/update/{id}")
    public String updateSpeaker(@PathVariable Long id, @ModelAttribute Speaker speaker) {
        speaker.setId(id); // retain ID to prevent save as new
        speakerRepository.save(speaker);
        return "redirect:/admin/speakers";
    }

    @GetMapping("/speaker/delete/{id}")
    public String deleteSpeaker(@PathVariable Long id) {
        speakerRepository.deleteById(id);
        return "redirect:/admin/speakers";
    }

    @GetMapping("/attendances")
    public String viewAllAttendances(Model model) {
        List<EventRegistration> allAttendances = registrationService.getAllAttendances();
        model.addAttribute("attendances", allAttendances);
        return "admin/attendances"; // Thymeleaf template: admin/attendances.html
    }
    @GetMapping("/registrations")
    public String viewRegistrations(Model model) {
        List<EventRegistration> registrations = registrationService.getAllRegistrations();
        model.addAttribute("registrations", registrations);
        return "admin/registrations";
    }

    @PostMapping("/registrations/update")
    public String updateAttendance(@RequestParam Long registrationId,
                                   @RequestParam Boolean attended) {
        EventRegistration registration = registrationService.getRegistrationById(registrationId);
        if (registration != null) {
            registration.setAttended(attended);
            registrationService.saveRegistration(registration);
        }
        return "redirect:/admin/registrations";
    }


}
