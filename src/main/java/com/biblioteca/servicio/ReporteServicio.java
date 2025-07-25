package com.biblioteca.servicio;

import com.biblioteca.dto.ReporteLibroMasPrestadoDTO;
import com.biblioteca.dto.ReporteUsuarioMasActivoDTO;
import com.biblioteca.repositorio.ReporteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReporteServicio {

    @Autowired
    private ReporteRepositorio reporteRepositorio;

    public List<ReporteLibroMasPrestadoDTO> obtenerLibrosMasPrestados() {
        return reporteRepositorio.obtenerLibrosMasPrestados();
    }

    public List<ReporteUsuarioMasActivoDTO> obtenerUsuariosMasActivos() {
        return reporteRepositorio.obtenerUsuariosMasActivos();
    }

}
