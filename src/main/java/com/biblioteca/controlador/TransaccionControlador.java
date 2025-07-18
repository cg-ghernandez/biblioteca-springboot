package com.biblioteca.controlador;

import com.biblioteca.modelo.Transaccion;
import com.biblioteca.servicio.TransaccionServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionControlador {

    @Autowired
    private TransaccionServicio transaccionServicio;

    @PostMapping
    public Transaccion registrarTransaccion(@RequestBody Transaccion transaccion) {
        transaccion.setEstado("PRESTAMO"); // Asignar estado automáticamente
        return transaccionServicio.guardarTransaccion(transaccion);
    }

    @GetMapping("/{id}")
    public Optional<Transaccion> obtenerTransaccionPorId(@PathVariable Long id) {
        return transaccionServicio.obtenerTransaccionPorId(id);
    }

    @GetMapping
    public List<Transaccion> listarTransacciones() {
        return transaccionServicio.listarTransacciones();
    }

    @GetMapping("/estado/{estado}")
    public List<Transaccion> listarPorEstado(@PathVariable String estado) {
        return transaccionServicio.listarPorEstado(estado);
    }

    @GetMapping("/pendientes")
    public List<Transaccion> obtenerTransaccionesPendientes() {
        return transaccionServicio.listarTransaccionesPendientes();
    }

    @DeleteMapping("/{id}")
    public void eliminarTransaccion(@PathVariable Long id) {
        transaccionServicio.eliminarTransaccion(id);
    }

}
