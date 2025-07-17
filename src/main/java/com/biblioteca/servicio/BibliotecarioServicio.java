package com.biblioteca.servicio;

import com.biblioteca.modelo.Bibliotecario;
import com.biblioteca.repositorio.BibliotecarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BibliotecarioServicio {

    @Autowired
    private BibliotecarioRepositorio bibliotecarioRepositorio;

    public List<Bibliotecario> listarTodos() {
        return bibliotecarioRepositorio.findAll();
    }

    public Optional<Bibliotecario> obtenerPorId(Long id) {
        return bibliotecarioRepositorio.findById(id);
    }

    public Bibliotecario guardar(Bibliotecario bibliotecario) {
        return bibliotecarioRepositorio.save(bibliotecario);
    }

    public void eliminar(Long id) {
        bibliotecarioRepositorio.deleteById(id);
    }

    public Optional<Bibliotecario> actualizar(Long id, Bibliotecario nuevoBibliotecario) {
        return bibliotecarioRepositorio.findById(id).map(b -> {
            b.setNombre(nuevoBibliotecario.getNombre());
            b.setEmail(nuevoBibliotecario.getEmail());
            b.setTelefono(nuevoBibliotecario.getTelefono());
            return bibliotecarioRepositorio.save(b);
        });
    }
}
