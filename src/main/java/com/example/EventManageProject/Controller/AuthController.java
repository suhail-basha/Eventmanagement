package com.example.EventManageProject.Controller;


import com.example.EventManageProject.Dto.UserDto;
import com.example.EventManageProject.Repositery.EventRepository;
import com.example.EventManageProject.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.EventManageProject.Entity.Event;

// ADMIN EMAIL - admin@gmail.com

// ADMIN PASSWORD - admin123


@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final EventRepository eventRepository;

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new UserDto());
        return "register"; // templates/register.html
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") UserDto dto, Model model) {
        userService.registerUser(dto);
        model.addAttribute("success", "Registration successful!");
        return "login"; // templates/login.html
    }

    @GetMapping("/")
    public String home(Model model) {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // templates/login.html
    }
    @GetMapping("/image/{id}")
    @ResponseBody
    public byte[] getImage(@PathVariable Long id) {
        return eventRepository.findById(id)
                .map(Event::getImage)
                .orElse(null);
    }
}
