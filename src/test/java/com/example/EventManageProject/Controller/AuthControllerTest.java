package com.example.EventManageProject.Controller;

import com.example.EventManageProject.Dto.UserDto;
import com.example.EventManageProject.Entity.Event;
import com.example.EventManageProject.Repositery.EventRepository;
import com.example.EventManageProject.Service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    private MockMvc mockMvc;
    private UserService userService;
    private EventRepository eventRepository;
    private AuthController authController;

    @BeforeEach
    void setup() {
        // Create mocks manually
        userService = Mockito.mock(UserService.class);
        eventRepository = Mockito.mock(EventRepository.class);


        authController = new AuthController(userService, eventRepository);


        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");  // dummy, won't actually resolve files here
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void testRegisterPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void testRegisterPost() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("name", "Test User")
                        .param("email", "testuser@example.com")
                        .param("password", "password123")
                        .param("phoneNumber", "1234567890")
                        .param("address", "Test Address")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("success"));

        Mockito.verify(userService).registerUser(any(UserDto.class));
    }

    @Test
    void testRootRedirect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testLoginPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void testGetImageSuccess() throws Exception {
        byte[] imageBytes = "dummyImage".getBytes();
        Event event = new Event();
        event.setImage(imageBytes);

        Mockito.when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        mockMvc.perform(MockMvcRequestBuilders.get("/image/1"))
                .andExpect(status().isOk())
                .andExpect(content().bytes(imageBytes));
    }

    @Test
    void testGetImageNotFound() throws Exception {
        Mockito.when(eventRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/image/99"))
                .andExpect(status().isOk())
                .andExpect(content().bytes(new byte[0]));
    }
}
