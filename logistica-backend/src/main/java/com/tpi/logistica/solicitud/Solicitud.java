package com.tpi.logistica.solicitud;

import com.tpi.logistica.cliente.Cliente;
import com.tpi.logistica.contenedor.Contenedor;
import com.tpi.logistica.ruta.Ruta;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "solicitud")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud")
    private Long id;

    // ---- Relaciones ----

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_contenedor", nullable = false)
    private Contenedor contenedor;

    @OneToOne(mappedBy = "solicitud", cascade = CascadeType.ALL)
    private Ruta ruta;

    // ---- Atributos ----

    private String estado;     // pendiente, asignada, en_proceso, finalizada

    @Column(name = "costo_estimado")
    private Double costoEstimado;

    @Column(name = "tiempo_estimado_min")
    private Integer tiempoEstimadoMin;

    @Column(name = "costo_final")
    private Double costoFinal;

    @Column(name = "tiempo_real_min")
    private Integer tiempoRealMin;

    // timestamps
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
