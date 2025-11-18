package com.tpi.logistica.costo;

import com.tpi.logistica.camion.Camion;
import com.tpi.logistica.deposito.Deposito;
import com.tpi.logistica.tarifa.Tarifa;
import com.tpi.logistica.tramo.Tramo;
import com.tpi.logistica.maps.DistanceResult;
import com.tpi.logistica.maps.MapsClient;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CostoServiceTest {

    @Test
    void calcularCostoRealUsesMapsClientDistance() throws Exception {
        CostoService svc = new CostoService();

        // inject a stub MapsClient via reflection
        MapsClient stub = (oLat, oLon, dLat, dLon) -> new DistanceResult(100.0, 120);
        Field mapsField = CostoService.class.getDeclaredField("mapsClient");
        mapsField.setAccessible(true);
        mapsField.set(svc, stub);

        // build tramo with tarifa and camion
        Tarifa tarifa = Tarifa.builder()
                .costoKmBase(10.0)
                .valorLitroCombustible(1.0)
                .costoGestionTramo(50.0)
                .build();

        Camion camion = Camion.builder()
                .consumoLtKm(0.2)
                .build();

        Deposito o = Deposito.builder().lat(0.0).lon(0.0).build();
        Deposito d = Deposito.builder().lat(1.0).lon(1.0).build();

        Tramo tramo = Tramo.builder()
                .tarifa(tarifa)
                .camion(camion)
                .depositoOrigen(o)
                .depositoDestino(d)
                .build();

        Double costoReal = svc.calcularCostoReal(tramo);

        // expected: costoKm = 10 * 100 = 1000
        // costoCombustible = consumo 0.2 * 100 * valorLitro 1.0 = 20
        // costoGestion = 50
        double expected = 1000.0 + 20.0 + 50.0;

        assertEquals(expected, costoReal, 0.0001);
    }
}
