package com.biblioteca.controlador;

import com.biblioteca.modelo.Multa;
import com.biblioteca.servicio.MultaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/multas")
public class MultaControlador {

    @Autowired
    private MultaServicio multaServicio;

    @GetMapping
    public List<Multa> listarMultas() {
        return multaServicio.listarMultas();
    }

    @GetMapping("/{id}")
    public Optional<Multa> obtenerMultaPorId(@PathVariable Long id) {
        return multaServicio.obtenerMultaPorId(id);
    }

    @PostMapping
    public Multa crearMulta(@RequestBody Multa multa) {
        return multaServicio.guardarMulta(multa);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMulta(@PathVariable Long id) {
        multaServicio.eliminarMulta(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<Multa> actualizarMulta(@PathVariable Long id, @RequestBody Multa multaActualizada) {
        return multaServicio.actualizarMulta(id, multaActualizada)
                .map(multa -> ResponseEntity.ok().body(multa))
                .orElse(ResponseEntity.notFound().build());
    }

}
