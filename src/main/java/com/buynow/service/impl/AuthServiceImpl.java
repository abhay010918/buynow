package com.buynow.service.impl;

import com.buynow.dto.LoginRequest;
import com.buynow.dto.SignupRequest;
import com.buynow.dto.UserResponse;
import com.buynow.entity.User;
import com.buynow.exeptions.ResourceNotFoundException;
import com.buynow.repository.UserRepository;
import com.buynow.service.AuthService;
import com.buynow.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public User signup(SignupRequest signupRequest) {

        if(userRepository.existsByEmail(signupRequest.getEmail())){
            throw new ResourceNotFoundException("email is already taken ");
        }

        String password = passwordEncoder.encode(signupRequest.getPassword());

        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setPhoneNumber(signupRequest.getPhoneNumber());
        user.setPassword(password);
        user.setRoles(signupRequest.getRole());

        return userRepository.save(user);
    }

    @Override
    public UserResponse login(LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new ResourceNotFoundException("Invalid password");
        }

        String token = jwtUtil.generateToken(user);

        return new UserResponse(token, user.getEmail(), user.getRoles().name());
    }
}
