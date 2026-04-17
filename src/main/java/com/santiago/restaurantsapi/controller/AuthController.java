package com.santiago.restaurantsapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.santiago.restaurantsapi.DTOs.JwtAuthenticationResponse;
import com.santiago.restaurantsapi.DTOs.LoginRequestDTO;
import com.santiago.restaurantsapi.service.AuthenticationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Autentica un usuario en el sistema.
     * Recibe credenciales válidas y retorna un token JWT si la autenticación es
     * exitosa.
     *
     * @param request DTO con email y contraseña del usuario
     * @return respuesta con el token JWT generado
     */

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody @Valid LoginRequestDTO request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }
}
