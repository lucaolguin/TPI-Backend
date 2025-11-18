package com.tpi.logistica.costo;

import com.tpi.logistica.maps.DistanceResult;
import com.tpi.logistica.maps.MapsClient;
import com.tpi.logistica.tramo.Tramo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CostoService {

    @Autowired(required = false)
    private MapsClient mapsClient;

    /**
     * Calcula y setea el costo real del tramo usando MapsClient si es posible.
     * Devuelve el costoReal calculado (o null si no se pudo calcular).
     */
    public Double calcularCostoReal(Tramo tramo) {
        if (tramo == null) return null;

        Double distanceKm = null;
        if (tramo.getDepositoOrigen() != null && tramo.getDepositoDestino() != null) {
            var o = tramo.getDepositoOrigen();
            var d = tramo.getDepositoDestino();
            DistanceResult res = mapsClient.getDistanceKmAndDurationMin(o.getLat(), o.getLon(), d.getLat(), d.getLon());
            if (res != null) distanceKm = res.getDistanceKm();
        }

        double estimatedKm = distanceKm == null ? 50.0 : distanceKm;

        if (tramo.getTarifa() == null || tramo.getCamion() == null) {
            // no podemos calcular sin tarifa o camion
            if (distanceKm != null) tramo.setCostoAprox(estimateAproxCost(tramo, estimatedKm));
            return null;
        }

        double costoKm = tramo.getTarifa().getCostoKmBase() == null ? 0.0 : tramo.getTarifa().getCostoKmBase() * estimatedKm;
        double consumoLtKm = tramo.getCamion().getConsumoLtKm() == null ? 0.0 : tramo.getCamion().getConsumoLtKm();
        double valorLitro = tramo.getTarifa().getValorLitroCombustible() == null ? 0.0 : tramo.getTarifa().getValorLitroCombustible();
        double costoCombustible = consumoLtKm * estimatedKm * valorLitro;
        double costoGestion = tramo.getTarifa().getCostoGestionTramo() == null ? 0.0 : tramo.getTarifa().getCostoGestionTramo();

        double recargoVol = 0.0;
        double recargoPeso = 0.0;
        if (tramo.getRuta() != null && tramo.getRuta().getSolicitud() != null && tramo.getRuta().getSolicitud().getContenedor() != null) {
            var cont = tramo.getRuta().getSolicitud().getContenedor();
            recargoVol = (tramo.getTarifa().getRecargoVolumenPorM3() == null ? 0.0 : tramo.getTarifa().getRecargoVolumenPorM3()) * (cont.getVolumenM3() == null ? 0.0 : cont.getVolumenM3());
            recargoPeso = (tramo.getTarifa().getRecargoPesoPorKg() == null ? 0.0 : tramo.getTarifa().getRecargoPesoPorKg()) * (cont.getPesoKg() == null ? 0.0 : cont.getPesoKg());
        }

        double costoReal = costoKm + costoCombustible + costoGestion + recargoVol + recargoPeso;
        tramo.setCostoReal(costoReal);
        // guardar aprox también
        tramo.setCostoAprox(estimateAproxCost(tramo, estimatedKm));
        return costoReal;
    }

    private Double estimateAproxCost(Tramo tramo, double km) {
        if (tramo.getTarifa() == null) return null;
        double costoKm = tramo.getTarifa().getCostoKmBase() == null ? 0.0 : tramo.getTarifa().getCostoKmBase() * km;
        double costoGestion = tramo.getTarifa().getCostoGestionTramo() == null ? 0.0 : tramo.getTarifa().getCostoGestionTramo();
        return costoKm + costoGestion;
    }
}
