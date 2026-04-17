package com.santiago.restaurantsapi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.santiago.restaurantsapi.DTOs.CoordinatesDTO;
import com.santiago.restaurantsapi.DTOs.RestaurantDTO;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Value("${geoapify.api.key}")
    private String apiKey;

    private final WebClient webClient = WebClient.create();
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Obtiene restaurantes cercanos a partir del nombre de una ciudad.
     *
     * @param city Nombre de la ciudad.
     * @return Lista de restaurantes cercanos.
     */

    public List<RestaurantDTO> getRestaurantsByCity(String city) {
        CoordinatesDTO coordinates = getCoordinates(city);
        return getRestaurantsByCoordinates(coordinates.getLat(), coordinates.getLng());
    }

    /**
     * Obtiene restaurantes cercanos a partir de unas coordenadas geográficas.
     *
     * @param lat Latitud de la ubicación.
     * @param lon Longitud de la ubicación.
     * @return Lista de restaurantes cercanos.
     * @throws RuntimeException si ocurre un error al procesar la respuesta de
     *                          Geoapify.
     */

    public List<RestaurantDTO> getRestaurantsByCoordinates(Double lat, Double lon) {
        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.geoapify.com")
                        .path("/v2/places")
                        .queryParam("categories", "catering.restaurant")
                        .queryParam("filter", "circle:" + lon + "," + lat + ",2000")
                        .queryParam("limit", 20)
                        .queryParam("apiKey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            JsonNode root = mapper.readTree(response);
            JsonNode features = root.get("features");

            List<RestaurantDTO> restaurants = new ArrayList<>();

            for (JsonNode feature : features) {
                JsonNode props = feature.get("properties");

                String name = props.has("name") ? props.get("name").asText() : "Sin nombre";
                String address = props.has("formatted") ? props.get("formatted").asText() : "Sin dirección";
                double restaurantLat = props.get("lat").asDouble();
                double restaurantLon = props.get("lon").asDouble();

                restaurants.add(new RestaurantDTO(name, address, restaurantLat, restaurantLon));
            }

            return restaurants;

        } catch (Exception e) {
            throw new RuntimeException("Error parsing restaurants response", e);
        }
    }

    /**
     * Obtiene las coordenadas de una ciudad a partir de la API de Geoapify.
     *
     * @param city Nombre de la ciudad.
     * @return Coordenadas de la ciudad consultada.
     * @throws RuntimeException si ocurre un error al procesar la respuesta de
     * Geoapify.
     */

    private CoordinatesDTO getCoordinates(String city) {
        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.geoapify.com")
                        .path("/v1/geocode/search")
                        .queryParam("text", city)
                        .queryParam("limit", 1)
                        .queryParam("apiKey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            JsonNode root = mapper.readTree(response);
            JsonNode props = root.get("features").get(0).get("properties");

            double lat = props.get("lat").asDouble();
            double lon = props.get("lon").asDouble();

            return new CoordinatesDTO(lat, lon);

        } catch (Exception e) {
            throw new RuntimeException("Error parsing coordinates response", e);
        }
    }
}