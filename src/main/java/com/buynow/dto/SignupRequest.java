package com.buynow.dto;

import com.buynow.enums.Role;
import lombok.Data;

@Data
public class SignupRequest {
    private String email;
    private String phoneNumber;
    private String password;
    private Role role;
}
