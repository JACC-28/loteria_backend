package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "estadisticas")
public class Estadistica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column
    private Integer partidasJugadas;

    @Column
    private Integer partidasGanadas;

    @Column
    private Integer boletosComprados;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getPartidasJugadas() {
        return partidasJugadas;
    }

    public void setPartidasJugadas(Integer partidasJugadas) {
        this.partidasJugadas = partidasJugadas;
    }

    public Integer getPartidasGanadas() {
        return partidasGanadas;
    }

    public void setPartidasGanadas(Integer partidasGanadas) {
        this.partidasGanadas = partidasGanadas;
    }

    public Integer getBoletosComprados() {
        return boletosComprados;
    }

    public void setBoletosComprados(Integer boletosComprados) {
        this.boletosComprados = boletosComprados;
    }
}