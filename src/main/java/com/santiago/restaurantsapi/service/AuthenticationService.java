package com.santiago.restaurantsapi.service;

import com.santiago.restaurantsapi.DTOs.JwtAuthenticationResponse;
import com.santiago.restaurantsapi.DTOs.LoginRequestDTO;

public interface AuthenticationService {
    JwtAuthenticationResponse login(LoginRequestDTO request);
}
