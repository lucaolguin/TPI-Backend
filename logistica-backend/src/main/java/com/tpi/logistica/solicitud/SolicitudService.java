package com.tpi.logistica.solicitud;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.tpi.logistica.cliente.Cliente;
import com.tpi.logistica.cliente.ClienteRepository;
import com.tpi.logistica.contenedor.Contenedor;
import com.tpi.logistica.contenedor.ContenedorRepository;
import com.tpi.logistica.dto.solicitud.SolicitudCreateDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final ClienteRepository clienteRepository;
    private final ContenedorRepository contenedorRepository;

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

        return solicitudRepository.save(solicitud);
    }

    public List<Solicitud> findAll() { return solicitudRepository.findAll(); }
    public Solicitud findById(Long id) { return solicitudRepository.findById(id).orElseThrow(); }
}


