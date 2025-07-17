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

    @GetMapping
    public List<Transaccion> listarTransacciones() {
        return transaccionServicio.listarTransacciones();
    }

    @GetMapping("/{id}")
    public Optional<Transaccion> obtenerPorId(@PathVariable Long id) {
        return transaccionServicio.obtenerTransaccionPorId(id);
    }

    @PostMapping
    public Transaccion guardar(@RequestBody Transaccion transaccion) {
        return transaccionServicio.guardarTransaccion(transaccion);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        transaccionServicio.eliminarTransaccion(id);
    }
}
