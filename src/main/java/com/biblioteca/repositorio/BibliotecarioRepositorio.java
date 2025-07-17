package com.biblioteca.repositorio;

import com.biblioteca.modelo.Bibliotecario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BibliotecarioRepositorio extends JpaRepository<Bibliotecario, Long> {
}
