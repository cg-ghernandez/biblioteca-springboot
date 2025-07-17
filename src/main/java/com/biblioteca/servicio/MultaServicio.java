package com.biblioteca.servicio;

import com.biblioteca.modelo.Multa;
import com.biblioteca.repositorio.MultaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MultaServicio {

    @Autowired
    private MultaRepositorio multaRepositorio;

    public List<Multa> listarMultas() {
        return multaRepositorio.findAll();
    }

    public Optional<Multa> obtenerMultaPorId(Long id) {
        return multaRepositorio.findById(id);
    }

    public Multa guardarMulta(Multa multa) {
        if (multa.getDevolucion() == null) {
            throw new RuntimeException("Debe asociar la multa a una devolución válida.");
        }

        // Verificar si ya existe una multa para esa devolución
        Optional<Multa> multaExistente = multaRepositorio.findAll().stream()
                .filter(m -> m.getDevolucion().getId().equals(multa.getDevolucion().getId()))
                .findFirst();

        if (multaExistente.isPresent()) {
            throw new RuntimeException("Ya existe una multa para esta devolución.");
        }

        return multaRepositorio.save(multa);
    }

    public void eliminarMulta(Long id) {
        multaRepositorio.deleteById(id);
    }

    public Optional<Multa> actualizarMulta(Long id, Multa nuevaMulta) {
        return multaRepositorio.findById(id).map(multaExistente -> {
            multaExistente.setMonto(nuevaMulta.getMonto());
            multaExistente.setFechaPago(nuevaMulta.getFechaPago());
            multaExistente.setPagada(nuevaMulta.isPagada());
            multaExistente.setDevolucion(nuevaMulta.getDevolucion());
            return multaRepositorio.save(multaExistente);
        });
    }

}
