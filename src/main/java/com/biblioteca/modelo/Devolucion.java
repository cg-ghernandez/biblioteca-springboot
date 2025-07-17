package com.biblioteca.modelo;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Devolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Transaccion transaccion;

    private LocalDate fechaDevolucion;

    private boolean conRetraso;

    private boolean enBuenEstado;

    @OneToOne(mappedBy = "devolucion", cascade = CascadeType.ALL)
    private Multa multa;
}
