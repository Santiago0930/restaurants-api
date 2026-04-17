package com.santiago.restaurantsapi.service;

import java.util.List;

import com.santiago.restaurantsapi.DTOs.RestaurantDTO;

public interface RestaurantService {
    List<RestaurantDTO> getRestaurantsByCity(String city);
    List<RestaurantDTO> getRestaurantsByCoordinates(Double lat, Double lon);
}