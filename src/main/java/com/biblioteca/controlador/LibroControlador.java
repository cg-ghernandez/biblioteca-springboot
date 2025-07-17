package com.biblioteca.controlador;

import com.biblioteca.modelo.Libro;
import com.biblioteca.servicio.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
public class LibroControlador {

    @Autowired
    private LibroServicio libroServicio;

    @GetMapping
    public List<Libro> listarLibros() {
        return libroServicio.listarLibros();
    }

    @GetMapping("/{id}")
    public Libro obtenerLibroPorId(@PathVariable Long id) {
        return libroServicio.obtenerLibroPorId(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
    }

    @PostMapping
    public Libro guardarLibro(@RequestBody Libro libro) {
        return libroServicio.guardarLibro(libro);
    }

    @PutMapping("/{id}")
    public Libro actualizarLibro(@PathVariable Long id, @RequestBody Libro libro) {
        return libroServicio.actualizarLibro(id, libro);
    }

    @DeleteMapping("/{id}")
    public void eliminarLibro(@PathVariable Long id) {
        libroServicio.eliminarLibro(id);
    }
}
