package com.biblioteca.servicio;

import com.biblioteca.modelo.Transaccion;
import com.biblioteca.repositorio.TransaccionRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransaccionServicio {

    @Autowired
    private TransaccionRepositorio transaccionRepositorio;

    public List<Transaccion> listarTransacciones() {
        return transaccionRepositorio.findAll();
    }

    public Optional<Transaccion> obtenerTransaccionPorId(Long id) {
        return transaccionRepositorio.findById(id);
    }

    public Transaccion guardarTransaccion(Transaccion transaccion) {
        return transaccionRepositorio.save(transaccion);
    }

    public void eliminarTransaccion(Long id) {
        transaccionRepositorio.deleteById(id);
    }
}
