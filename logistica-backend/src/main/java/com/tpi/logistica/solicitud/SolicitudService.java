package com.tpi.logistica.solicitud;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.tpi.logistica.cliente.Cliente;
import com.tpi.logistica.cliente.ClienteRepository;
import com.tpi.logistica.contenedor.Contenedor;
import com.tpi.logistica.contenedor.ContenedorRepository;
import com.tpi.logistica.dto.solicitud.SolicitudCreateDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final ClienteRepository clienteRepository;
    private final ContenedorRepository contenedorRepository;
    private final com.tpi.logistica.ruta.RutaRepository rutaRepository;
    private final com.tpi.logistica.deposito.DepositoRepository depositoRepository;
    private final com.tpi.logistica.tramo.TramoRepository tramoRepository;
    private final com.tpi.logistica.camion.CamionRepository camionRepository;
    private final com.tpi.logistica.tarifa.TarifaRepository tarifaRepository;

    public Solicitud crearSolicitud(SolicitudCreateDto dto) {

        // Validar cliente
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Validar contenedor
        Contenedor contenedor = contenedorRepository.findById(dto.getContenedorId())
                .orElseThrow(() -> new RuntimeException("Contenedor no encontrado"));

        // Validar que el contenedor pertenezca al cliente
        if (!contenedor.getCliente().getId().equals(cliente.getId())) {
            throw new RuntimeException("El contenedor no pertenece al cliente indicado");
        }

        // Crear solicitud
        Solicitud solicitud = new Solicitud();
        solicitud.setCliente(cliente);
        solicitud.setContenedor(contenedor);

        solicitud.setEstado("PENDIENTE");
        solicitud.setCostoEstimado(0.0);
        solicitud.setTiempoEstimadoMin(0);
        
        solicitud.setCostoFinal(null);
        solicitud.setTiempoRealMin(null);
        solicitud.setCreatedAt(LocalDateTime.now());
        solicitud.setUpdatedAt(LocalDateTime.now());

        return solicitudRepository.save(solicitud);
    }

    public List<Solicitud> findAll() { return solicitudRepository.findAll(); }
    public Solicitud findById(Long id) { return solicitudRepository.findById(id).orElseThrow(); }

    // Asigna/crea una Ruta mínima estimada para la solicitud indicada.
    public com.tpi.logistica.ruta.Ruta asignarRuta(Long solicitudId) {
        Solicitud solicitud = findById(solicitudId);

        if (solicitud.getRuta() != null) {
            // ya tiene ruta asignada, devolvemos la existente
            return solicitud.getRuta();
        }

        com.tpi.logistica.ruta.Ruta ruta = new com.tpi.logistica.ruta.Ruta();
        ruta.setSolicitud(solicitud);
        ruta.setCantidadTramos(0);
        ruta.setCantidadDepositos(0);
        ruta.setCreatedAt(LocalDateTime.now());
        ruta.setUpdatedAt(LocalDateTime.now());

        com.tpi.logistica.ruta.Ruta guardada = rutaRepository.save(ruta);

        // intentar generar un tramo estimado si existen datos mínimos (deposito, camion, tarifa)
        var depositos = depositoRepository.findAll();
        var camiones = camionRepository.findAll();
        var tarifas = tarifaRepository.findAll();

        if (!depositos.isEmpty() && !camiones.isEmpty() && !tarifas.isEmpty()) {
            com.tpi.logistica.tramo.Tramo tramo = new com.tpi.logistica.tramo.Tramo();
            tramo.setRuta(guardada);
            tramo.setCamion(camiones.get(0));
            tramo.setDepositoOrigen(depositos.get(0));
            tramo.setDepositoDestino(depositos.get(0));
            tramo.setTarifa(tarifas.get(0));
            tramo.setTipo("origen-destino");
            tramo.setEstado("estimado");
            tramo.setCostoAprox(0.0);
            tramo.setCostoReal(null);
            tramo.setDestinoDireccion("");
            tramo.setCreatedAt(LocalDateTime.now());
            tramo.setUpdatedAt(LocalDateTime.now());

            tramoRepository.save(tramo);

            // actualizar conteos en ruta
            guardada.setCantidadTramos(1);
            guardada.setCantidadDepositos(1);
            rutaRepository.save(guardada);
        }

        // ligar la ruta a la solicitud y actualizar
        solicitud.setRuta(guardada);
        solicitud.setUpdatedAt(LocalDateTime.now());
        solicitudRepository.save(solicitud);

        return guardada;
    }
}


