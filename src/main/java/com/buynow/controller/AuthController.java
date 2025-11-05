package com.buynow.controller;

import com.buynow.dto.LoginRequest;
import com.buynow.dto.SignupRequest;
import com.buynow.dto.UserResponse;
import com.buynow.entity.User;
import com.buynow.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody SignupRequest signupRequest){
        User signup = authService.signup(signupRequest);
        return ResponseEntity.ok(signup);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest loginRequest) {
        UserResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}
