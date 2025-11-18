package com.tpi.logistica.maps;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HaversineMapsClientTest {

    @Test
    void samePointReturnsZeroDistanceAndMinDuration() {
        HaversineMapsClient client = new HaversineMapsClient();
        DistanceResult res = client.getDistanceKmAndDurationMin(0.0, 0.0, 0.0, 0.0);
        assertNotNull(res);
        assertEquals(0.0, res.getDistanceKm(), 0.0001);
        assertTrue(res.getDurationMin() >= 1);
    }

    @Test
    void calculatesPositiveDistanceBetweenPoints() {
        HaversineMapsClient client = new HaversineMapsClient();
        // Example: approximate distance between two nearby points
        DistanceResult res = client.getDistanceKmAndDurationMin(-34.6037, -58.3816, -34.9011, -56.1645);
        assertNotNull(res);
        assertTrue(res.getDistanceKm() > 0);
        // duration should roughly match rounded distance (implementation detail)
        assertEquals(Math.round(res.getDistanceKm()), res.getDurationMin());
    }
}
