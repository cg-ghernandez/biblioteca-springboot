package com.biblioteca.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DevolucionDTO {
    private Long id;
    private String libro;
    private String usuario;
    private LocalDate fechaDevolucion;
    private boolean conRetraso;
    private boolean enBuenEstado;
    private long diasRetraso;
    private BigDecimal multaMonto;
    private boolean multaPagada;
}
