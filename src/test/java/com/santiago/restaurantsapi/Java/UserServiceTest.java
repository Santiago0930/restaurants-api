package com.santiago.restaurantsapi.Java;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.santiago.restaurantsapi.DTOs.UserRequestDto;
import com.santiago.restaurantsapi.DTOs.UserResponseDto;
import com.santiago.restaurantsapi.model.User;
import com.santiago.restaurantsapi.repository.UserRepository;
import com.santiago.restaurantsapi.service.TransactionService;
import com.santiago.restaurantsapi.service.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private TransactionService transactionService;

    @Test
    @DisplayName("Debe registrar un usuario satisfactoriamente")
    public void testRegisterUser_successful() {

        UserRequestDto dto = UserRequestDto.builder()
                .email("test@email.com")
                .password("1234")
                .firstName("Brayan")
                .lastName("Guerrero")
                .age(30)
                .build();

        String passwordCodificada = "hashedPassword";
        when(passwordEncoder.encode("1234")).thenReturn(passwordCodificada);

        User usuarioGuardado = User.builder()
                .id(1L)
                .email(dto.getEmail())
                .password(passwordCodificada)
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .age(dto.getAge())
                .build();

        when(userRepository.save(any(User.class))).thenReturn(usuarioGuardado);

        UserResponseDto resultado = userService.registerUser(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(dto.getEmail(), resultado.getEmail());
        assertEquals(dto.getFirstName(), resultado.getFirstName());
        assertEquals(dto.getLastName(), resultado.getLastName());

        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("1234");
    }

    @Test
     @DisplayName("Debe impedir registrar el usuario ya que este ya esta en el sistema")
    public void testRegisterUser_Failed() {

        UserRequestDto dto = UserRequestDto.builder()
                .email("ya@registrado.com")
                .password("1234")
                .firstName("Santiago")
                .lastName("Guerrero")
                .age(23)
                .build();

        User existente = User.builder()
                .id(1L)
                .email(dto.getEmail())
                .password("hashedPassword")
                .firstName("Otro")
                .firstName("Usuario")
                .age(30)
                .build();

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(existente));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(dto);
        });

        assertEquals("El email ya esta registrado", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }
}
