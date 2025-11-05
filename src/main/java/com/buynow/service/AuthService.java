package com.buynow.service;

import com.buynow.dto.LoginRequest;
import com.buynow.dto.SignupRequest;
import com.buynow.dto.UserResponse;
import com.buynow.entity.User;



public interface AuthService {

    public User signup(SignupRequest signupRequest);
    public UserResponse login(LoginRequest loginRequest);

}
