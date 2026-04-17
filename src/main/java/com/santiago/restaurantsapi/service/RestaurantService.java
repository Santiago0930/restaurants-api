package com.santiago.restaurantsapi.service;

import java.util.List;

import com.santiago.restaurantsapi.DTOs.RestaurantDTO;
import com.santiago.restaurantsapi.model.User;

public interface RestaurantService {
    List<RestaurantDTO> getRestaurantsByCity(String city, User user);
    List<RestaurantDTO> getRestaurantsByCoordinates(Double lat, Double lon, User user);
}