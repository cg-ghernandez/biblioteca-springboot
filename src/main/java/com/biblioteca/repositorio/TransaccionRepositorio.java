package com.biblioteca.repositorio;

import com.biblioteca.modelo.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransaccionRepositorio extends JpaRepository<Transaccion, Long> {
}
