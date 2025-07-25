package com.biblioteca.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReporteUsuarioMasActivoDTO {
    private String nombre;
    private String email;
    private Long cantidadPrestamos;
}
