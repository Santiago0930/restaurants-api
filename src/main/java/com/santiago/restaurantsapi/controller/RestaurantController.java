package com.santiago.restaurantsapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.santiago.restaurantsapi.DTOs.RestaurantDTO;
import com.santiago.restaurantsapi.service.RestaurantService;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    /**
     * Retorna restaurantes cercanos según una ciudad o coordenadas.
     *
     * @param city Nombre de la ciudad (opcional).
     * @param lat  Latitud (requerida si no se envía ciudad).
     * @param lon  Longitud (requerida si no se envía ciudad).
     * @return Lista de restaurantes cercanos.
     * @throws IllegalArgumentException si no se envía ciudad ni coordenadas
     *                                  válidas.
     */

    @GetMapping("/nearby")
    public ResponseEntity<List<RestaurantDTO>> getNearbyRestaurants(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lon) {

        if (city == null && (lat == null || lon == null)) {
            throw new IllegalArgumentException("Debe enviar una ciudad o las coordenadas lat y lon.");
        }

        if (city != null && (!city.isBlank())) {
            return ResponseEntity.ok(restaurantService.getRestaurantsByCity(city));
        }

        return ResponseEntity.ok(restaurantService.getRestaurantsByCoordinates(lat, lon));
    }
}