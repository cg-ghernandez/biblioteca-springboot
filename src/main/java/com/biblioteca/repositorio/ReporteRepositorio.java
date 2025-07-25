package com.biblioteca.repositorio;

import com.biblioteca.dto.ReporteLibroMasPrestadoDTO;
import com.biblioteca.modelo.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReporteRepositorio extends JpaRepository<Transaccion, Long> {

    @Query("SELECT new com.biblioteca.dto.ReporteLibroMasPrestadoDTO(t.libro.titulo, t.libro.autor, COUNT(t)) " +
            "FROM Transaccion t WHERE t.tipo = 'PRESTAMO' " +
            "GROUP BY t.libro.titulo, t.libro.autor " +
            "ORDER BY COUNT(t) DESC")
    List<ReporteLibroMasPrestadoDTO> obtenerLibrosMasPrestados();

}
