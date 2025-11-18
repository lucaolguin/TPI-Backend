package com.tpi.logistica.maps;

public interface MapsClient {
    /**
     * Retorna la distancia en kilómetros y duración en minutos entre dos puntos.
     * Si alguno de los puntos es null devuelve null.
     */
    DistanceResult getDistanceKmAndDurationMin(Double originLat, Double originLon, Double destLat, Double destLon);
}
