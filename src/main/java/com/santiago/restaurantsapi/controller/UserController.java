package com.santiago.restaurantsapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.santiago.restaurantsapi.DTOs.UserRequestDto;
import com.santiago.restaurantsapi.DTOs.UserResponseDto;
import com.santiago.restaurantsapi.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * Endpoint que registra un nuevo usuario en el sistema.
     * Recibe los datos del nuevo usuario, los valida mediante @Valid e invoca el
     * servicio de creación de usuario.
     * 
     * @param newUser DTO con la información necesaria para crear un usuario
     *                (nombre, email, contraseña, etc).
     *
     * @return ResponseEntity con los datos del usuario registrado (No retorna
     *         información sensible como la contraseña).
     */
    @PostMapping("/registerUser")
    public ResponseEntity<UserResponseDto> registrarUsuario(@RequestBody @Valid UserRequestDto newUser) {
        return ResponseEntity.ok(userService.registerUser(newUser));
    }

}
