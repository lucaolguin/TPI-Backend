package com.tpi.logistica.maps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@ConditionalOnProperty(prefix = "maps.google", name = "api-key")
public class GoogleMapsClient implements MapsClient {

    private final String apiKey;
    private final HttpClient httpClient;
    private final ObjectMapper mapper = new ObjectMapper();
    private final String baseUrl;

    public GoogleMapsClient(@Value("${maps.google.api-key}") String apiKey) {
        this(apiKey, HttpClient.newHttpClient(), "https://maps.googleapis.com/");
    }

    // Default constructor for frameworks that require it (e.g. some DI containers)
    public GoogleMapsClient() {
        this(null, HttpClient.newHttpClient(), "https://maps.googleapis.com/");
    }

    // Test / integration friendly constructor
    public GoogleMapsClient(String apiKey, HttpClient httpClient, String baseUrl) {
        this.apiKey = apiKey;
        this.httpClient = httpClient == null ? HttpClient.newHttpClient() : httpClient;
        if (baseUrl == null || baseUrl.isBlank()) this.baseUrl = "https://maps.googleapis.com/";
        else this.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
    }

    @Override
    public DistanceResult getDistanceKmAndDurationMin(Double originLat, Double originLon, Double destLat, Double destLon) {
        if (originLat == null || originLon == null || destLat == null || destLon == null) return null;

        try {
            String origin = URLEncoder.encode(originLat + "," + originLon, StandardCharsets.UTF_8);
            String dest = URLEncoder.encode(destLat + "," + destLon, StandardCharsets.UTF_8);
            String url = String.format("%smaps/api/directions/json?origin=%s&destination=%s&key=%s&units=metric", baseUrl, origin, dest, URLEncoder.encode(apiKey, StandardCharsets.UTF_8));

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<InputStream> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofInputStream());
            if (resp.statusCode() != 200) return null;

            JsonNode root = mapper.readTree(resp.body());
            JsonNode routes = root.path("routes");
            if (!routes.isArray() || routes.size() == 0) return null;
            JsonNode legs = routes.get(0).path("legs");
            if (!legs.isArray() || legs.size() == 0) return null;
            JsonNode leg = legs.get(0);
            JsonNode distance = leg.path("distance");
            JsonNode duration = leg.path("duration");
            if (distance.isMissingNode() || duration.isMissingNode()) return null;

            double meters = distance.path("value").asDouble(0.0);
            int seconds = duration.path("value").asInt(0);

            double km = meters / 1000.0;
            int minutes = Math.max(1, (int) Math.round(seconds / 60.0));

            return new DistanceResult(km, minutes);
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
}
