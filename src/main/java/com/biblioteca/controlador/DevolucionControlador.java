package com.biblioteca.controlador;

import com.biblioteca.modelo.Devolucion;
import com.biblioteca.servicio.DevolucionServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}")
    public Optional<Devolucion> actualizar(@PathVariable Long id, @RequestBody Devolucion devolucion) {
        return devolucionServicio.actualizarDevolucion(id, devolucion);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        devolucionServicio.eliminarDevolucion(id);
    }
}
