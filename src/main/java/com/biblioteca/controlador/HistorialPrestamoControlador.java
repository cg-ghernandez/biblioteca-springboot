package com.biblioteca.controlador;

import com.biblioteca.modelo.HistorialPrestamo;
import com.biblioteca.servicio.HistorialPrestamoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historial")
public class HistorialPrestamoControlador {

    @Autowired
    private HistorialPrestamoServicio historialServicio;

    @GetMapping
    public List<HistorialPrestamo> listarTodos() {
        return historialServicio.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistorialPrestamo> obtenerPorId(@PathVariable Long id) {
        return historialServicio.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public HistorialPrestamo crear(@RequestBody HistorialPrestamo historial) {
        return historialServicio.guardar(historial);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistorialPrestamo> actualizar(@PathVariable Long id, @RequestBody HistorialPrestamo nuevoHistorial) {
        return historialServicio.actualizar(id, nuevoHistorial)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        historialServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
