package com.example.demo.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "boletos")
public class Boleto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "partida_id") // Clave foránea que relaciona con Partida
    private Partida partida;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToMany
    @JoinTable(
            name = "boleto_numero",
            joinColumns = @JoinColumn(name = "boleto_id"),
            inverseJoinColumns = @JoinColumn(name = "numero_id")
    )

    private List<Numero> numeros;


    @ManyToOne
    @JoinColumn(name = "tablero_id") // Clave foránea que apunta a Tablero
    private Tablero tablero;

    public Tablero getTablero() {
        return tablero;
    }

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Numero> getNumeros() {
        return numeros;
    }

    public void setNumeros(List<Numero> numeros) {
        this.numeros = numeros;
    }


}
