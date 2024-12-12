package com.example.demo.repository;

import com.example.demo.model.Estadistica;
import com.example.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadisticaRepository extends JpaRepository<Estadistica, Long> {

    // Métodos personalizados (opcionales, pero útiles)
    Optional<Estadistica> findByUsuario(Usuario usuario);

    // Otros métodos que podrías necesitar:
    // List<Estadistica> findByPartidasJugadasGreaterThan(int partidasJugadas);
    // List<Estadistica> findByPartidasGanadasBetween(int minPartidasGanadas, int maxPartidasGanadas);
    // ...
}