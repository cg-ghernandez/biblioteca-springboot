package com.biblioteca.repositorio;

import com.biblioteca.modelo.Multa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MultaRepositorio extends JpaRepository<Multa, Long> {
}
