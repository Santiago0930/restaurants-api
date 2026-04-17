package com.santiago.restaurantsapi.Java;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import com.santiago.restaurantsapi.controller.RestaurantController;
import com.santiago.restaurantsapi.DTOs.RestaurantDTO;
import com.santiago.restaurantsapi.config.JwtAuthenticationFilter;
import com.santiago.restaurantsapi.model.User;
import com.santiago.restaurantsapi.service.JwtService;
import com.santiago.restaurantsapi.service.RestaurantService;
import com.santiago.restaurantsapi.service.UserService;

@WebMvcTest(RestaurantController.class)
@AutoConfigureMockMvc(addFilters = false)
class RestaurantControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtService jwtService;

    @Test
    @DisplayName("Debe retornar restaurantes cuando se consulta por ciudad")
    public void getNearbyRestaurants_ByCity_Successful() throws Exception {
        String email = "santiago@test.com";

        User user = User.builder()
                .id(1L)
                .email(email)
                .firstName("Santiago")
                .lastName("Guerrero")
                .age(23)
                .build();

        List<RestaurantDTO> restaurants = List.of(
                new RestaurantDTO("Andres DC", "Bogota", 4.65, -74.08),
                new RestaurantDTO("Crepes", "Bogota", 4.66, -74.07));

        Authentication auth = new UsernamePasswordAuthenticationToken(email, null, List.of());

        when(userService.findByEmail(email)).thenReturn(user);
        when(restaurantService.getRestaurantsByCity("Bogota", user)).thenReturn(restaurants);

        mockMvc.perform(get("/restaurant/nearby")
                .param("city", "Bogota")
                .principal(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Andres DC"))
                .andExpect(jsonPath("$[1].name").value("Crepes"));

        verify(userService).findByEmail(email);
        verify(restaurantService).getRestaurantsByCity("Bogota", user);
    }

    @Test
    @DisplayName("Debe retornar restaurantes cuando se consulta por coordenadas")
    public void getNearbyRestaurants_ByCoordinates_Successful() throws Exception {
        String email = "santiago@test.com";

        User user = User.builder()
                .id(1L)
                .email(email)
                .firstName("Santiago")
                .lastName("Guerrero")
                .age(23)
                .build();

        List<RestaurantDTO> restaurants = List.of(
                new RestaurantDTO("Corral", "Bogota", 4.6533, -74.0836),
                new RestaurantDTO("Crepes", "Bogota", 4.6540, -74.0820));

        Authentication auth = new UsernamePasswordAuthenticationToken(email, null, List.of());

        when(userService.findByEmail(email)).thenReturn(user);
        when(restaurantService.getRestaurantsByCoordinates(4.6533, -74.0836, user))
                .thenReturn(restaurants);

        mockMvc.perform(get("/restaurant/nearby")
                .param("lat", "4.6533")
                .param("lon", "-74.0836")
                .principal(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Corral"))
                .andExpect(jsonPath("$[0].lat").value(4.6533))
                .andExpect(jsonPath("$[0].lng").value(-74.0836))
                .andExpect(jsonPath("$[1].name").value("Crepes"));

        verify(userService).findByEmail(email);
        verify(restaurantService).getRestaurantsByCoordinates(4.6533, -74.0836, user);
    }
}