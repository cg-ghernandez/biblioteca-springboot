package com.biblioteca.repositorio;

import com.biblioteca.modelo.Devolucion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DevolucionRepositorio extends JpaRepository<Devolucion, Long> {
}
