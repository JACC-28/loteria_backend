package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "numeros")
public class Numero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tablero_id")
    private Tablero tablero;

// Getters and Setters for tablero

    @Column
    private Integer valor;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }
}