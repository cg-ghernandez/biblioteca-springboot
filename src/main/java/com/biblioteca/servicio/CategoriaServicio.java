package com.biblioteca.servicio;

import com.biblioteca.modelo.Categoria;
import com.biblioteca.repositorio.CategoriaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaServicio {

    @Autowired
    private CategoriaRepositorio categoriaRepositorio;

    public List<Categoria> listarCategorias() {
        return categoriaRepositorio.findAll();
    }

    public Optional<Categoria> obtenerCategoriaPorId(Long id) {
        return categoriaRepositorio.findById(id);
    }

    public Categoria guardarCategoria(Categoria categoria) {
        return categoriaRepositorio.save(categoria);
    }

    public void eliminarCategoria(Long id) {
        categoriaRepositorio.deleteById(id);
    }

    public Optional<Categoria> actualizarCategoria(Long id, Categoria nuevaCategoria) {
        return categoriaRepositorio.findById(id).map(categoriaExistente -> {
            categoriaExistente.setNombre(nuevaCategoria.getNombre());
            return categoriaRepositorio.save(categoriaExistente);
        });
    }
}
