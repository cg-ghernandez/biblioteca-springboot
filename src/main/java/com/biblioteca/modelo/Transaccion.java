package com.biblioteca.modelo;

import com.biblioteca.modelo.enums.TipoTransaccion;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaccion {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "libro_id")
    private Libro libro;

    @ManyToOne
    @JoinColumn(name = "bibliotecario_id")
    private Bibliotecario bibliotecario;

    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;

    @Enumerated(EnumType.STRING)
    private TipoTransaccion tipo;
}
