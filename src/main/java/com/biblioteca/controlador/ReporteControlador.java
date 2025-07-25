package com.biblioteca.controlador;

import com.biblioteca.dto.ReporteLibroMasPrestadoDTO;
import com.biblioteca.servicio.ReporteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
public class ReporteControlador {

    @Autowired
    private ReporteServicio reporteServicio;

    @GetMapping("/libros-mas-prestados")
    public List<ReporteLibroMasPrestadoDTO> librosMasPrestados() {
        return reporteServicio.obtenerLibrosMasPrestados();
    }
}
