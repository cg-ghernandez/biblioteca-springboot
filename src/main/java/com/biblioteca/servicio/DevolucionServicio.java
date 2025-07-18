package com.biblioteca.servicio;

import com.biblioteca.modelo.Devolucion;
import com.biblioteca.modelo.Multa;
import com.biblioteca.modelo.Transaccion;
import com.biblioteca.repositorio.DevolucionRepositorio;
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

    public List<Devolucion> listarDevoluciones() {
        return devolucionRepositorio.findAll();
    }

    public Optional<Devolucion> obtenerDevolucionPorId(Long id) {
        return devolucionRepositorio.findById(id);
    }

    public Devolucion guardarDevolucion(Devolucion devolucion) {
        Transaccion transaccion = transaccionRepositorio.findById(devolucion.getTransaccion().getId())
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada"));

        // Determinar si hay retraso
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

        // Guardar devolución
        Devolucion devolucionGuardada = devolucionRepositorio.save(devolucion);

        // Generar multa si hay condiciones
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

        return devolucionGuardada;
    }

    public void eliminarDevolucion(Long id) {
        devolucionRepositorio.deleteById(id);
    }

    public Optional<Devolucion> actualizarDevolucion(Long id, Devolucion nuevaDevolucion) {
        return devolucionRepositorio.findById(id).map(devolucionExistente -> {
            devolucionExistente.setFechaDevolucion(nuevaDevolucion.getFechaDevolucion());
            devolucionExistente.setEnBuenEstado(nuevaDevolucion.isEnBuenEstado());
            devolucionExistente.setTransaccion(nuevaDevolucion.getTransaccion());
            return devolucionRepositorio.save(devolucionExistente);
        });
    }
}
