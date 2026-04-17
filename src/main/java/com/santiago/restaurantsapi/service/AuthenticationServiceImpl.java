package com.santiago.restaurantsapi.service;

import com.santiago.restaurantsapi.DTOs.JwtAuthenticationResponse;
import com.santiago.restaurantsapi.DTOs.LoginRequestDTO;
import com.santiago.restaurantsapi.model.ActionType;
import com.santiago.restaurantsapi.model.User;
import com.santiago.restaurantsapi.repository.UserRepository;

import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TransactionService transactionService;

    public AuthenticationServiceImpl(TransactionService transactionService, AuthenticationManager authenticationManager,
            JwtService jwtService, UserRepository userRepository) {
        this.transactionService = transactionService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    /**
     * Autentica un usuario y genera un token JWT si las credenciales son válidas.
     * Valida las credenciales con AuthenticationManager y verifica la existencia
     * del usuario.
     *
     * @param request DTO con email y contraseña del usuario
     * @return JwtAuthenticationResponse con token JWT y datos del usuario
     *         autenticado
     */

    @Override
    public JwtAuthenticationResponse login(LoginRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Email no encontrado."));

        transactionService.saveTransaction(ActionType.LOGIN, user);

        String jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt, user.getEmail(), "User", user.getFirstName());
    }
}