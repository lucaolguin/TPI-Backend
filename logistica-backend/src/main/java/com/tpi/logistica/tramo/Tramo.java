package com.tpi.logistica.tramo;

import com.tpi.logistica.camion.Camion;
import com.tpi.logistica.deposito.Deposito;
import com.tpi.logistica.ruta.Ruta;
import com.tpi.logistica.tarifa.Tarifa;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tramo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tramo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tramo")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_ruta", nullable = false)
    private Ruta ruta;

    @ManyToOne
    @JoinColumn(name = "id_camion", nullable = false)
    private Camion camion;

    @ManyToOne
    @JoinColumn(name = "id_deposito_origen", nullable = false)
    private Deposito depositoOrigen;

    @ManyToOne
    @JoinColumn(name = "id_deposito_destino", nullable = false)
    private Deposito depositoDestino;

    @ManyToOne
    @JoinColumn(name = "id_tarifa", nullable = false)
    private Tarifa tarifa;

    private String tipo;      // origen-destino, deposito-deposito…

    private String estado;    // estimado, asignado, iniciado, finalizado

    @Column(name = "costo_aprox")
    private Double costoAprox;

    @Column(name = "costo_real")
    private Double costoReal;

    @Column(name = "fecha_hora_inicio")
    private LocalDateTime fechaHoraInicio;

    @Column(name = "fecha_hora_fin")
    private LocalDateTime fechaHoraFin;

    @Column(name = "destino_direccion")
    private String destinoDireccion;

    // timestamps
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
