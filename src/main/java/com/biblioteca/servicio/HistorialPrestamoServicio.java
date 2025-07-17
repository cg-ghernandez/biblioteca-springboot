package com.biblioteca.servicio;

import com.biblioteca.modelo.HistorialPrestamo;
import com.biblioteca.repositorio.HistorialPrestamoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistorialPrestamoServicio {

    @Autowired
    private HistorialPrestamoRepositorio historialRepositorio;

    public List<HistorialPrestamo> listarTodos() {
        return historialRepositorio.findAll();
    }

    public Optional<HistorialPrestamo> obtenerPorId(Long id) {
        return historialRepositorio.findById(id);
    }

    public HistorialPrestamo guardar(HistorialPrestamo historial) {
        return historialRepositorio.save(historial);
    }

    public void eliminar(Long id) {
        historialRepositorio.deleteById(id);
    }

    public Optional<HistorialPrestamo> actualizar(Long id, HistorialPrestamo nuevoHistorial) {
        return historialRepositorio.findById(id).map(h -> {
            h.setUsuario(nuevoHistorial.getUsuario());
            h.setLibro(nuevoHistorial.getLibro());
            h.setFechaPrestamo(nuevoHistorial.getFechaPrestamo());
            h.setFechaDevolucion(nuevoHistorial.getFechaDevolucion());
            h.setDevuelto(nuevoHistorial.isDevuelto());
            h.setEstado(nuevoHistorial.getEstado());
            return historialRepositorio.save(h);
        });
    }
}
