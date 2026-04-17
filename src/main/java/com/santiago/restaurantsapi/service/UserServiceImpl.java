package com.santiago.restaurantsapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.santiago.restaurantsapi.DTOs.UserRequestDto;
import com.santiago.restaurantsapi.DTOs.UserResponseDto;
import com.santiago.restaurantsapi.model.ActionType;
import com.santiago.restaurantsapi.model.User;
import com.santiago.restaurantsapi.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    
    @Lazy
    private final PasswordEncoder passwordEncoder;

    private final TransactionService transactionService;

    public UserServiceImpl(TransactionService transactionService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.transactionService = transactionService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * Recibe los datos del usuario desde el controlador,
     * valida que el email no este previamente registrado en el sistema, crea una
     * nueva entidad User a partir de los datos recibidos por el DTO y la persiste
     * en la base de datos.
     * 
     * @param dto DTO con la información del usuario a registrar
     * @return UserResponseDto con los datos del usuario creado (sin información
     *         sensible)
     * @throws RuntimeException si el email ya se encuentra registrado
     */
    @Transactional
    @Override
    public UserResponseDto registerUser(UserRequestDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya esta registrado");
        }

        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .age(dto.getAge())
                .build();

        User savedUser = userRepository.save(user);

        transactionService.saveTransaction(ActionType.SIGN_UP, user);

        return UserResponseDto.builder()
                .id(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .build();
    }

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                return userRepository.findByEmail(email)
                        .map(user -> (UserDetails) user)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
            }
        };
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }
}
