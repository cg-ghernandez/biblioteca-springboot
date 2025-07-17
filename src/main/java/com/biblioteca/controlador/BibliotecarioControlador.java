package com.biblioteca.controlador;

import com.biblioteca.modelo.Bibliotecario;
import com.biblioteca.servicio.BibliotecarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bibliotecarios")
public class BibliotecarioControlador {

    @Autowired
    private BibliotecarioServicio bibliotecarioServicio;

    @GetMapping
    public List<Bibliotecario> listarTodos() {
        return bibliotecarioServicio.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bibliotecario> obtenerPorId(@PathVariable Long id) {
        return bibliotecarioServicio.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Bibliotecario crear(@RequestBody Bibliotecario bibliotecario) {
        return bibliotecarioServicio.guardar(bibliotecario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bibliotecario> actualizar(@PathVariable Long id, @RequestBody Bibliotecario nuevoBibliotecario) {
        return bibliotecarioServicio.actualizar(id, nuevoBibliotecario)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        bibliotecarioServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
