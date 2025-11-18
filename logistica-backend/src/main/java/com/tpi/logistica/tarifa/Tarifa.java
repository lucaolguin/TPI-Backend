package com.tpi.logistica.tarifa;

import com.tpi.logistica.tramo.Tramo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tarifa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tarifa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarifa")
    private Long id;

    private String nombre;

    @Column(name = "vigencia_desde")
    private LocalDateTime vigenciaDesde;

    @Column(name = "vigencia_hasta")
    private LocalDateTime vigenciaHasta;

    @Column(name = "valor_litro_combustible")
    private Double valorLitroCombustible;

    @Column(name = "costo_km_base")
    private Double costoKmBase;

    @Column(name = "costo_gestion_tramo")
    private Double costoGestionTramo;

    @Column(name = "recargo_volumen_por_m3")
    private Double recargoVolumenPorM3;

    @Column(name = "recargo_peso_por_kg")
    private Double recargoPesoPorKg;

    @OneToMany(mappedBy = "tarifa", cascade = CascadeType.ALL)
    private List<Tramo> tramos;

    // timestamps
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
