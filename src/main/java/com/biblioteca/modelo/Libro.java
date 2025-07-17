package com.biblioteca.modelo;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Libro {
    @Id @GeneratedValue
    private Long id;
    private String titulo;
    private String autor;
    private String isbn;
    private boolean disponible;
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    @JsonBackReference
    private Categoria categoria;
}
