package com.biblioteca.controlador;

import com.biblioteca.modelo.Devolucion;
import com.biblioteca.servicio.DevolucionServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.biblioteca.dto.DevolucionDTO;
import java.time.LocalDate;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/devoluciones")
public class DevolucionControlador {

    @Autowired
    private DevolucionServicio devolucionServicio;

    @GetMapping
    public List<Devolucion> listar() {
        return devolucionServicio.listarDevoluciones();
    }

    @GetMapping("/{id}")
    public Optional<Devolucion> obtenerPorId(@PathVariable Long id) {
        return devolucionServicio.obtenerDevolucionPorId(id);
    }

    @PostMapping
    public Devolucion guardar(@RequestBody Devolucion devolucion) {
        return devolucionServicio.guardarDevolucion(devolucion);
    }

    @GetMapping("/historial")
    public List<DevolucionDTO> listarDevolucionesDTO() {
        return devolucionServicio.listarDevoluciones().stream().map(dev -> {
            DevolucionDTO dto = new DevolucionDTO();
            dto.setId(dev.getId());
            dto.setFechaDevolucion(dev.getFechaDevolucion());
            dto.setConRetraso(dev.isConRetraso());
            dto.setEnBuenEstado(dev.isEnBuenEstado());

            // Calcular días de retraso
            if (dev.isConRetraso() && dev.getTransaccion() != null) {
                LocalDate esperado = dev.getTransaccion().getFechaDevolucion();
                LocalDate real = dev.getFechaDevolucion();
                if (esperado != null && real != null && real.isAfter(esperado)) {
                    dto.setDiasRetraso(java.time.temporal.ChronoUnit.DAYS.between(esperado, real));
                }
            } else {
                dto.setDiasRetraso(0);
            }

            // Info del usuario y libro
            if (dev.getTransaccion() != null) {
                dto.setUsuario(dev.getTransaccion().getUsuario().getNombre());
                dto.setLibro(dev.getTransaccion().getLibro().getTitulo());
            }

            // Info de multa
            if (dev.getMulta() != null) {
                dto.setMultaMonto(dev.getMulta().getMonto());
                dto.setMultaPagada(dev.getMulta().isPagada());
            }

            return dto;
        }).toList();
    }

    @PutMapping("/{id}")
    public Optional<Devolucion> actualizar(@PathVariable Long id, @RequestBody Devolucion devolucion) {
        return devolucionServicio.actualizarDevolucion(id, devolucion);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        devolucionServicio.eliminarDevolucion(id);
    }
}
