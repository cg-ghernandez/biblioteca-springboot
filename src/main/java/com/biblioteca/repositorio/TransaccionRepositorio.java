package com.biblioteca.repositorio;

import com.biblioteca.modelo.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransaccionRepositorio extends JpaRepository<Transaccion, Long> {

    List<Transaccion> findByEstado(String estado);
}
