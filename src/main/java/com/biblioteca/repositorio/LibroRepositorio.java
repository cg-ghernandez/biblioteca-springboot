package com.biblioteca.repositorio;

import com.biblioteca.modelo.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, Long> {
    List<Libro> findByDisponible(boolean disponible);
}
