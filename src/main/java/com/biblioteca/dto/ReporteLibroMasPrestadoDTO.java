package com.biblioteca.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReporteLibroMasPrestadoDTO {
    private String titulo;
    private String autor;
    private Long cantidadPrestamos;
}
