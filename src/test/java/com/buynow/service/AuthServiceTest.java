package com.buynow.service;


import com.buynow.dto.SignupRequest;
import com.buynow.entity.User;
import com.buynow.enums.Role;
import com.buynow.exeptions.ResourceNotFoundException;
import com.buynow.repository.UserRepository;
import com.buynow.service.impl.AuthServiceImpl;
import com.buynow.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import software.amazon.awssdk.awscore.util.AwsHeader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    // -------------- SIGNUP TEST ------------------

    @Test
    void tetSignup_success(){

        SignupRequest request = new SignupRequest();

        request.setEmail("test@gmail.com");
        request.setPassword("abahy123");
        request.setPhoneNumber("8085460159");
        request.setRole(Role.ADMIN);

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail(request.getEmail());
        savedUser.setPassword("encodedPassword");
        savedUser.setPhoneNumber(request.getPhoneNumber());
        savedUser.setRoles(request.getRole());

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = authService.signup(request);

        assertNotNull(result);
        assertEquals("test@gmail.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testSignupEmailAlreadyExists(){

        SignupRequest request = new SignupRequest();

        request.setEmail("abhay@gmail.com");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(ResourceNotFoundException.class, () -> authService.signup(request));

        verify(userRepository, never()).save(any(User.class));

    }
}
