package com.biblioteca.repositorio;

import com.biblioteca.dto.ReporteLibroMasPrestadoDTO;
import com.biblioteca.dto.ReporteUsuarioMasActivoDTO;
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

    @Query("SELECT new com.biblioteca.dto.ReporteUsuarioMasActivoDTO(t.usuario.nombre, t.usuario.email, COUNT(t)) " +
            "FROM Transaccion t WHERE t.tipo = 'PRESTAMO' " +
            "GROUP BY t.usuario.nombre, t.usuario.email " +
            "ORDER BY COUNT(t) DESC")
    List<ReporteUsuarioMasActivoDTO> obtenerUsuariosMasActivos();

}
