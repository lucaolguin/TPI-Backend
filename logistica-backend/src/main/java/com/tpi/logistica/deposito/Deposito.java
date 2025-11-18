package com.tpi.logistica.deposito;

import com.tpi.logistica.tramo.Tramo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "deposito")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Deposito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_deposito")
    private Long id;

    private String nombre;

    private String direccion;

    private Double lat;

    private Double lon;

    @Column(name = "costo_estadia_diario")
    private Double costoEstadiaDiario;

    @OneToMany(mappedBy = "depositoOrigen", cascade = CascadeType.ALL)
    private List<Tramo> tramosComoOrigen;

    @OneToMany(mappedBy = "depositoDestino", cascade = CascadeType.ALL)
    private List<Tramo> tramosComoDestino;

    // timestamps
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
