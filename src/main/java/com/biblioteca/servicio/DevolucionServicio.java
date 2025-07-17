package com.biblioteca.servicio;

import com.biblioteca.modelo.Devolucion;
import com.biblioteca.repositorio.DevolucionRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DevolucionServicio {

    @Autowired
    private DevolucionRepositorio devolucionRepositorio;

    public List<Devolucion> listarDevoluciones() {
        return devolucionRepositorio.findAll();
    }

    public Optional<Devolucion> obtenerDevolucionPorId(Long id) {
        return devolucionRepositorio.findById(id);
    }

    public Devolucion guardarDevolucion(Devolucion devolucion) {
        return devolucionRepositorio.save(devolucion);
    }

    public void eliminarDevolucion(Long id) {
        devolucionRepositorio.deleteById(id);
    }

    public Optional<Devolucion> actualizarDevolucion(Long id, Devolucion nuevaDevolucion) {
        return devolucionRepositorio.findById(id).map(devolucionExistente -> {
            devolucionExistente.setFechaDevolucion(nuevaDevolucion.getFechaDevolucion());
            devolucionExistente.setEnBuenEstado(nuevaDevolucion.isEnBuenEstado());
            devolucionExistente.setTransaccion(nuevaDevolucion.getTransaccion());
            return devolucionRepositorio.save(devolucionExistente);
        });
    }
}
