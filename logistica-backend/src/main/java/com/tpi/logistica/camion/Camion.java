package com.tpi.logistica.camion;

import com.tpi.logistica.tramo.Tramo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "camion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Camion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_camion")
    private Long id;

    private String dominio;

    @Column(name = "nombre_transportista")
    private String nombreTransportista;

    private String telefono;

    @Column(name = "capacidad_peso_kg")
    private Double capacidadPesoKg;

    @Column(name = "capacidad_vol_m3")
    private Double capacidadVolM3;

    @Column(name = "costo_base_km")
    private Double costoBaseKm;

    @Column(name = "consumo_lt_km")
    private Double consumoLtKm;

    private Boolean disponible;

    // relación con tramo
    @OneToMany(mappedBy = "camion", cascade = CascadeType.ALL)
    private List<Tramo> tramos;

    // timestamps
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
