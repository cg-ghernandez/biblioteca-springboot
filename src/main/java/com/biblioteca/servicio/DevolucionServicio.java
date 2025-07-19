package com.biblioteca.servicio;

import com.biblioteca.modelo.Devolucion;
import com.biblioteca.modelo.Libro;
import com.biblioteca.modelo.Multa;
import com.biblioteca.modelo.Transaccion;
import com.biblioteca.repositorio.DevolucionRepositorio;
import com.biblioteca.repositorio.LibroRepositorio;
import com.biblioteca.repositorio.MultaRepositorio;
import com.biblioteca.repositorio.TransaccionRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class DevolucionServicio {

    @Autowired
    private DevolucionRepositorio devolucionRepositorio;

    @Autowired
    private TransaccionRepositorio transaccionRepositorio;

    @Autowired
    private MultaRepositorio multaRepositorio;

    @Autowired
    private LibroRepositorio libroRepositorio;

    public List<Devolucion> listarDevoluciones() {
        return devolucionRepositorio.findAll();
    }

    public Optional<Devolucion> obtenerDevolucionPorId(Long id) {
        return devolucionRepositorio.findById(id);
    }

    public Devolucion guardarDevolucion(Devolucion devolucion) {
        Transaccion transaccion = transaccionRepositorio.findById(devolucion.getTransaccion().getId())
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada"));

        LocalDate fechaEsperada = transaccion.getFechaDevolucion();
        LocalDate fechaReal = devolucion.getFechaDevolucion();
        long diasRetraso = 0;
        boolean conRetraso = false;

        if (fechaReal != null && fechaEsperada != null && fechaReal.isAfter(fechaEsperada)) {
            conRetraso = true;
            diasRetraso = ChronoUnit.DAYS.between(fechaEsperada, fechaReal);
            devolucion.setConRetraso(true);
        } else {
            devolucion.setConRetraso(false);
        }

        devolucion.setTransaccion(transaccion);

        Devolucion devolucionGuardada = devolucionRepositorio.save(devolucion);

        if (conRetraso || !devolucion.isEnBuenEstado()) {
            BigDecimal monto = BigDecimal.ZERO;

            if (conRetraso) {
                monto = monto.add(BigDecimal.valueOf(1000L * diasRetraso));
            }

            if (!devolucion.isEnBuenEstado()) {
                monto = monto.add(BigDecimal.valueOf(5000));
            }

            Multa multa = new Multa();
            multa.setDevolucion(devolucionGuardada);
            multa.setMonto(monto);
            multa.setFechaPago(null);
            multa.setPagada(false);

            multaRepositorio.save(multa);
        }

        Libro libro = transaccion.getLibro();
        libro.setDisponible(true);
        libroRepositorio.save(libro);

        transaccion.setEstado("DEVUELTO");
        transaccionRepositorio.save(transaccion);

        return devolucionGuardada;
    }

    public void eliminarDevolucion(Long id) {
        devolucionRepositorio.deleteById(id);
    }

    public Optional<Devolucion> actualizarDevolucion(Long id, Devolucion nuevaDevolucion) {
        return devolucionRepositorio.findById(id).map(dev -> {
            // Actualizar fecha y estado
            dev.setFechaDevolucion(nuevaDevolucion.getFechaDevolucion());
            dev.setEnBuenEstado(nuevaDevolucion.isEnBuenEstado());

            Transaccion transaccion = transaccionRepositorio.findById(nuevaDevolucion.getTransaccion().getId())
                    .orElseThrow(() -> new RuntimeException("Transacción no encontrada"));

            dev.setTransaccion(transaccion);

            // Recalcular si hay retraso
            LocalDate esperado = transaccion.getFechaDevolucion();
            LocalDate real = nuevaDevolucion.getFechaDevolucion();

            boolean conRetraso = esperado != null && real != null && real.isAfter(esperado);
            dev.setConRetraso(conRetraso);

            // Calcular nueva multa
            if (conRetraso || !nuevaDevolucion.isEnBuenEstado()) {
                long diasRetraso = conRetraso ? ChronoUnit.DAYS.between(esperado, real) : 0;
                BigDecimal monto = BigDecimal.ZERO;

                if (conRetraso) {
                    monto = monto.add(BigDecimal.valueOf(1000L * diasRetraso));
                }

                if (!nuevaDevolucion.isEnBuenEstado()) {
                    monto = monto.add(BigDecimal.valueOf(5000));
                }

                // Si ya existe multa, actualizarla
                if (dev.getMulta() != null) {
                    dev.getMulta().setMonto(monto);
                    dev.getMulta().setFechaPago(null);
                    dev.getMulta().setPagada(false);
                    multaRepositorio.save(dev.getMulta());
                } else {
                    // Crear nueva multa
                    Multa nueva = new Multa();
                    nueva.setDevolucion(dev);
                    nueva.setMonto(monto);
                    nueva.setPagada(false);
                    nueva.setFechaPago(null);
                    multaRepositorio.save(nueva);
                }

            } else {
                // Si no hay condiciones, eliminar la multa si existe
                if (dev.getMulta() != null) {
                    multaRepositorio.delete(dev.getMulta());
                    dev.setMulta(null);
                }
            }

            return devolucionRepositorio.save(dev);
        });
    }
}
