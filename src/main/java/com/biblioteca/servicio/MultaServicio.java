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
