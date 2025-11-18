package com.tpi.logistica.maps;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingBean(com.tpi.logistica.maps.MapsClient.class)
public class HaversineMapsClient implements MapsClient {

    private static final double EARTH_RADIUS_KM = 6371.0;

    @Override
    public DistanceResult getDistanceKmAndDurationMin(Double originLat, Double originLon, Double destLat, Double destLon) {
        if (originLat == null || originLon == null || destLat == null || destLon == null) return null;

        double dLat = Math.toRadians(destLat - originLat);
        double dLon = Math.toRadians(destLon - originLon);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                   Math.cos(Math.toRadians(originLat)) * Math.cos(Math.toRadians(destLat)) *
                   Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distanceKm = EARTH_RADIUS_KM * c;

        // Estimación de duración usando velocidad media 60 km/h
        int durationMin = (int) Math.max(1, Math.round((distanceKm / 60.0) * 60.0));

        return new DistanceResult(distanceKm, durationMin);
    }
}
