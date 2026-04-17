package com.santiago.restaurantsapi.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.santiago.restaurantsapi.DTOs.UserRequestDto;
import com.santiago.restaurantsapi.DTOs.UserResponseDto;

public interface UserService {
    UserResponseDto registerUser(UserRequestDto request);
    UserDetailsService userDetailsService();
}