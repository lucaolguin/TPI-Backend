package com.tpi.logistica.tramo;

import com.tpi.logistica.camion.Camion;
import com.tpi.logistica.camion.CamionRepository;
import com.tpi.logistica.contenedor.Contenedor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TramoService {

    private final TramoRepository tramoRepository;
    private final CamionRepository camionRepository;
    private final com.tpi.logistica.solicitud.SolicitudRepository solicitudRepository;

    public List<Tramo> findAll() {
        return tramoRepository.findAll();
    }

    public Tramo findById(Long id) {
        return tramoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tramo no encontrado"));
    }

    public Tramo create(Tramo tramo) {
        return tramoRepository.save(tramo);
    }

    public Tramo update(Long id, Tramo tramo) {
        Tramo existente = findById(id);
        tramo.setId(existente.getId());
        return tramoRepository.save(tramo);
    }

    public void delete(Long id) {
        tramoRepository.deleteById(id);
    }

    /**
     * Asigna un camión a un tramo: valida disponibilidad y capacidades.
     */
    public Tramo asignarCamion(Long tramoId, Long camionId) {
        Tramo tramo = findById(tramoId);

        Camion camion = camionRepository.findById(camionId)
                .orElseThrow(() -> new RuntimeException("Camión no encontrado"));

        if (camion.getDisponible() == null || !camion.getDisponible()) {
            throw new RuntimeException("El camión no está disponible");
        }

        // Validar capacidad respecto al contenedor asociado a la solicitud de la ruta
        Contenedor contenedor = null;
        if (tramo.getRuta() != null && tramo.getRuta().getSolicitud() != null) {
            contenedor = tramo.getRuta().getSolicitud().getContenedor();
        }

        if (contenedor != null) {
            Double peso = contenedor.getPesoKg() == null ? 0.0 : contenedor.getPesoKg();
            Double volumen = contenedor.getVolumenM3() == null ? 0.0 : contenedor.getVolumenM3();

            if (camion.getCapacidadPesoKg() != null && peso > camion.getCapacidadPesoKg()) {
                throw new RuntimeException("El camión no soporta el peso del contenedor");
            }
            if (camion.getCapacidadVolM3() != null && volumen > camion.getCapacidadVolM3()) {
                throw new RuntimeException("El camión no soporta el volumen del contenedor");
            }
        }

        // Asignar y marcar no disponible
        tramo.setCamion(camion);
        tramo.setEstado("asignado");
        camion.setDisponible(false);

        camionRepository.save(camion);
        return tramoRepository.save(tramo);
    }

    /**
     * Marca el inicio del tramo: fija fechaHoraInicio y cambia estado a 'iniciado'.
     */
    public Tramo iniciarTramo(Long tramoId) {
        Tramo tramo = findById(tramoId);

        if (tramo.getEstado() != null && tramo.getEstado().equalsIgnoreCase("iniciado")) {
            return tramo; // ya iniciado
        }

        tramo.setFechaHoraInicio(java.time.LocalDateTime.now());
        tramo.setEstado("iniciado");

        return tramoRepository.save(tramo);
    }

    /**
     * Marca el fin del tramo: fija fechaHoraFin, calcula costo real (estimado aquí) y libera el camión.
     */
    public Tramo finalizarTramo(Long tramoId) {
        Tramo tramo = findById(tramoId);

        if (tramo.getFechaHoraInicio() == null) {
            throw new RuntimeException("El tramo no fue iniciado");
        }

        tramo.setFechaHoraFin(java.time.LocalDateTime.now());
        tramo.setEstado("finalizado");

        // Calcular costo real de forma estimada si contamos con tarifa y camión
        Double costoReal = null;
        if (tramo.getTarifa() != null && tramo.getCamion() != null) {
            double estimatedKm = 50.0; // valor temporal hasta integrar MapsClient
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

            costoReal = costoKm + costoCombustible + costoGestion + recargoVol + recargoPeso;
            tramo.setCostoReal(costoReal);
        }

        // Liberar camión si existe
        if (tramo.getCamion() != null) {
            var camion = tramo.getCamion();
            camion.setDisponible(true);
            camionRepository.save(camion);
        }

        Tramo guardado = tramoRepository.save(tramo);

        // Actualizar costo/tiempo en la solicitud asociada si corresponde
        if (guardado.getRuta() != null && guardado.getRuta().getSolicitud() != null) {
            var solicitud = guardado.getRuta().getSolicitud();
            // recalcular costo final sumando costos reales de tramos finalizados
            double sumaCostos = guardado.getRuta().getTramos() == null ? 0.0 : guardado.getRuta().getTramos().stream()
                    .filter(t -> t.getCostoReal() != null)
                    .mapToDouble(t -> t.getCostoReal()).sum();
            solicitud.setCostoFinal(sumaCostos);

            // tiempo real: diferencia entre primer inicio y último fin en minutos
            var tramos = guardado.getRuta().getTramos();
            if (tramos != null) {
                java.time.LocalDateTime primero = tramos.stream().map(Tramo::getFechaHoraInicio).filter(d -> d != null).min(java.time.LocalDateTime::compareTo).orElse(null);
                java.time.LocalDateTime ultimo = tramos.stream().map(Tramo::getFechaHoraFin).filter(d -> d != null).max(java.time.LocalDateTime::compareTo).orElse(null);
                if (primero != null && ultimo != null) {
                    long mins = java.time.Duration.between(primero, ultimo).toMinutes();
                    solicitud.setTiempoRealMin((int) mins);
                }
            }

            // actualizar estado de la solicitud si todos los tramos están finalizados
            boolean todosFinalizados = guardado.getRuta().getTramos() == null || guardado.getRuta().getTramos().stream().allMatch(t -> "finalizado".equalsIgnoreCase(t.getEstado()));
            if (todosFinalizados) solicitud.setEstado("finalizada");

            solicitud.setUpdatedAt(java.time.LocalDateTime.now());
            // guardar solicitud
            var solicitudRepo = solicitudRepository;
            solicitudRepo.save(solicitud);
        }

        return guardado;
    }
}
