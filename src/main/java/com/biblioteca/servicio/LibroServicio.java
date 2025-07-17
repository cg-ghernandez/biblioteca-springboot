package com.biblioteca.servicio;

import com.biblioteca.modelo.Libro;
import com.biblioteca.repositorio.LibroRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibroServicio {

    @Autowired
    private LibroRepositorio libroRepositorio;

    // Listar todos los libros
    public List<Libro> listarLibros() {
        return libroRepositorio.findAll();
    }

    // Obtener un libro por ID
    public Optional<Libro> obtenerLibroPorId(Long id) {
        return libroRepositorio.findById(id);
    }

    // Guardar un nuevo libro
    public Libro guardarLibro(Libro libro) {
        return libroRepositorio.save(libro);
    }

    // Eliminar un libro por ID
    public void eliminarLibro(Long id) {
        libroRepositorio.deleteById(id);
    }

    // Actualizar un libro existente
    public Libro actualizarLibro(Long id, Libro libroActualizado) {
        return libroRepositorio.findById(id).map(libro -> {
            libro.setTitulo(libroActualizado.getTitulo());
            libro.setAutor(libroActualizado.getAutor());
            libro.setIsbn(libroActualizado.getIsbn());
            libro.setDisponible(libroActualizado.isDisponible());
            libro.setCategoria(libroActualizado.getCategoria()); // importante para mantener la relación
            return libroRepositorio.save(libro);
        }).orElse(null);
    }
}
