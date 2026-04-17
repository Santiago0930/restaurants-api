package com.santiago.restaurantsapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.santiago.restaurantsapi.DTOs.JwtAuthenticationResponse;
import com.santiago.restaurantsapi.DTOs.LoginRequestDTO;
import com.santiago.restaurantsapi.model.ActionType;
import com.santiago.restaurantsapi.model.User;
import com.santiago.restaurantsapi.service.AuthenticationService;
import com.santiago.restaurantsapi.service.TransactionService;
import com.santiago.restaurantsapi.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final TransactionService transactionService;
    private final UserService userService;

    public AuthController(TransactionService transactionService, UserService userService,
            AuthenticationService authenticationService) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

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

    /**
     * Permite al usuario autenticado cerrar sesión y registra la acción de logout.
     *
     * @param authentication Contiene los datos del usuario autenticado (JWT).
     * @return Respuesta con mensaje de logout exitoso.
     */

    @PostMapping("/logout")
    public ResponseEntity<String> logout(Authentication authentication) {

        String email = authentication.getName();
        User user = userService.findByEmail(email);

        transactionService.saveTransaction(ActionType.LOGOUT, user);

        return ResponseEntity.ok("Logout exitoso");
    }
}
