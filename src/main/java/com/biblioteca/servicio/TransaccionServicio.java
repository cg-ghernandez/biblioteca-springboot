package com.biblioteca.servicio;

import com.biblioteca.modelo.HistorialPrestamo;
import com.biblioteca.modelo.Libro;
import com.biblioteca.modelo.Transaccion;
import com.biblioteca.repositorio.TransaccionRepositorio;
import com.biblioteca.repositorio.LibroRepositorio;
import com.biblioteca.repositorio.HistorialPrestamoRepositorio;
import com.biblioteca.modelo.enums.TipoTransaccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransaccionServicio {

    @Autowired
    private TransaccionRepositorio transaccionRepositorio;

    @Autowired
    private LibroRepositorio libroRepositorio;

    @Autowired
    private HistorialPrestamoRepositorio historialRepositorio;

    public List<Transaccion> listarTransacciones() {
        return transaccionRepositorio.findAll();
    }

    public Optional<Transaccion> obtenerTransaccionPorId(Long id) {
        return transaccionRepositorio.findById(id);
    }

    public Transaccion guardarTransaccion(Transaccion transaccion) {
        Libro libro = libroRepositorio.findById(transaccion.getLibro().getId())
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        if (transaccion.getTipo() == TipoTransaccion.PRESTAMO) {
            if (!libro.isDisponible()) {
                throw new RuntimeException("El libro no está disponible para préstamo.");
            }
            libro.setDisponible(false);
            libroRepositorio.save(libro);
        }

        if (transaccion.getTipo() == TipoTransaccion.DEVOLUCION) {
            libro.setDisponible(true);
            libroRepositorio.save(libro);
        }

        Transaccion transaccionGuardada = transaccionRepositorio.save(transaccion);

        if (transaccion.getTipo() == TipoTransaccion.PRESTAMO) {
            HistorialPrestamo historial = new HistorialPrestamo();
            historial.setUsuario(transaccion.getUsuario());
            historial.setLibro(transaccion.getLibro());
            historial.setFechaPrestamo(transaccion.getFechaPrestamo());
            historial.setDevuelto(false);
            historial.setEstado("Pendiente");
            historialRepositorio.save(historial);
        }

        return transaccionGuardada;
    }

    public void eliminarTransaccion(Long id) {
        transaccionRepositorio.deleteById(id);
    }

    public List<Transaccion> listarTransaccionesPendientes() {
        return transaccionRepositorio.findByEstado("PRESTAMO");
    }

    public List<Transaccion> listarPorEstado(String estado) {
        return transaccionRepositorio.findByEstado(estado);
    }


}
