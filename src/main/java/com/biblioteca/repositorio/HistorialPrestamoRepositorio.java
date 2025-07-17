package com.biblioteca.repositorio;

import com.biblioteca.modelo.HistorialPrestamo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistorialPrestamoRepositorio extends JpaRepository<HistorialPrestamo, Long> {
}
