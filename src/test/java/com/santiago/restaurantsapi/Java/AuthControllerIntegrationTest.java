package com.santiago.restaurantsapi.Java;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.santiago.restaurantsapi.DTOs.LoginRequestDTO;
import com.santiago.restaurantsapi.DTOs.UserRequestDto;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Debe acceder correctamente al sistema")
    public void login_successful() throws Exception {
        String email = "test" + System.currentTimeMillis() + "@test.com";

        UserRequestDto nuevoUsuario = UserRequestDto.builder()
                .email(email)
                .password("1234")
                .firstName("Esteban")
                .lastName("Guerrero")
                .age(23)
                .build();

        String response = mockMvc.perform(post("/user/registerUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoUsuario)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserRequestDto usuarioRegistrado = objectMapper.readValue(response, UserRequestDto.class);

        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail(usuarioRegistrado.getEmail());
        request.setPassword(nuevoUsuario.getPassword());

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("Debe botar error porque las credenciales son invalidas")
    public void login_Failed() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("usuario@email.com");
        request.setPassword("contrasena_invalida");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError());
    }
}