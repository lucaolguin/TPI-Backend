package com.tpi.logistica.tramo;

import com.tpi.logistica.camion.Camion;
import com.tpi.logistica.camion.CamionRepository;
import com.tpi.logistica.costo.CostoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TramoServiceTest {

    private TramoRepository tramoRepository;
    private CamionRepository camionRepository;
    private com.tpi.logistica.solicitud.SolicitudRepository solicitudRepository;
    private CostoService costoService;
    private TramoService tramoService;

    @BeforeEach
    void setUp() {
        tramoRepository = Mockito.mock(TramoRepository.class);
        camionRepository = Mockito.mock(CamionRepository.class);
        solicitudRepository = Mockito.mock(com.tpi.logistica.solicitud.SolicitudRepository.class);
        costoService = Mockito.mock(CostoService.class);

        tramoService = new TramoService(tramoRepository, camionRepository, solicitudRepository, costoService);
    }

    @Test
    void iniciarTramo_setsFechaInicioAndEstado() {
        Tramo tramo = Tramo.builder().id(1L).build();
        when(tramoRepository.findById(1L)).thenReturn(Optional.of(tramo));
        when(tramoRepository.save(any(Tramo.class))).thenAnswer(inv -> inv.getArgument(0));

        Tramo result = tramoService.iniciarTramo(1L);

        assertNotNull(result.getFechaHoraInicio());
        assertEquals("iniciado", result.getEstado());
    }

    @Test
    void finalizarTramo_setsFechaFin_estado_and_liberaCamion() {
        Camion camion = Camion.builder().id(10L).disponible(false).build();
        Tramo tramo = Tramo.builder().id(2L).camion(camion).fechaHoraInicio(java.time.LocalDateTime.now().minusMinutes(5)).build();

        when(tramoRepository.findById(2L)).thenReturn(Optional.of(tramo));
        when(costoService.calcularCostoReal(any(Tramo.class))).thenReturn(123.45);
        when(tramoRepository.save(any(Tramo.class))).thenAnswer(inv -> inv.getArgument(0));
        when(camionRepository.save(any(Camion.class))).thenAnswer(inv -> inv.getArgument(0));

        Tramo result = tramoService.finalizarTramo(2L);

        assertNotNull(result.getFechaHoraFin());
        assertEquals("finalizado", result.getEstado());
        assertEquals(123.45, result.getCostoReal());
        assertTrue(result.getCamion().getDisponible());
    }
}
