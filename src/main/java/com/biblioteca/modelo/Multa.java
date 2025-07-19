package com.biblioteca.modelo;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Multa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "devolucion_id")
    //@JsonBackReference
    @JsonManagedReference
    private Devolucion devolucion;

    private BigDecimal monto;

    private LocalDate fechaPago;

    private boolean pagada;
}
