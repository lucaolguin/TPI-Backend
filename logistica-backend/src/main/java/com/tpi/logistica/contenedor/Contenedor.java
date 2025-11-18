package com.tpi.logistica.contenedor;

import com.tpi.logistica.cliente.Cliente;
import com.tpi.logistica.solicitud.Solicitud;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "contenedor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contenedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contenedor")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @Column(name = "peso_kg")
    private Double pesoKg;

    @Column(name = "volumen_m3")
    private Double volumenM3;

    // Ej: "pendiente", "asignado", "en_transito", "entregado"
    private String estado;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Una misma carga (contenedor) puede tener varias solicitudes en el tiempo
    @OneToMany(mappedBy = "contenedor", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Solicitud> solicitudes;
}
