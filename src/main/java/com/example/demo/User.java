package com.example.demo;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    // Puntaje acumulado del jugador
    private Integer puntaje;

    // Si admin es "admin" => administrador, si es null => usuario normal
    private String admin;

    @Column(name = "partidas_ganadas")
    private Integer partidasGanadas;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) { this.id = id; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) { this.password = password; }

    public Integer getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(Integer puntaje) { this.puntaje = puntaje; }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) { this.admin = admin; }

    public Integer getPartidasGanadas() {
        return partidasGanadas;
    }

    public void setPartidasGanadas(Integer partidasGanadas) { this.partidasGanadas = partidasGanadas; }
}
