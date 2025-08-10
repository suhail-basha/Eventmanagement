package com.example.EventManageProject.Service;
import com.example.EventManageProject.Entity.EventRegistration;
import com.example.EventManageProject.Entity.User;
import com.example.EventManageProject.Repositery.EventRegistrationRepository;
import com.example.EventManageProject.Repositery.EventRepository;
import com.example.EventManageProject.Repositery.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import org.modelmapper.ModelMapper;
@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final EventRepository eventRepository;
    private final EventRegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ModelMapper modelMapper;
    private final EventRegistrationRepository eventRegistrationRepository;

    public List<EventRegistration> getRecentRegistrations() {
        return registrationRepository.findTop5ByOrderByRegisteredAtDesc();
    }
    public boolean isUserRegistered(Long userId, Long eventId) {
        return registrationRepository.existsByUserIdAndEventId(userId, eventId);
    }
    public void saveRegistration(EventRegistration registration) {
        registrationRepository.save(registration);
    }
    public List<EventRegistration> getRegistrationsByUser(User user) {
        return registrationRepository.findByUser(user);
    }
    public EventRegistration findByUserIdAndEventId(Long userId, Long eventId) {
        return eventRegistrationRepository.findByUserIdAndEventId(userId, eventId).orElse(null);
    }
    public List<EventRegistration> getAllRegistrations() {
        return registrationRepository.findAll();
    }

    public EventRegistration getRegistrationById(Long id) {
        return registrationRepository.findById(id).orElse(null);
    }
    public List<EventRegistration> getAllAttendances() {
        return eventRegistrationRepository.getAllAttendances();
    }


}
