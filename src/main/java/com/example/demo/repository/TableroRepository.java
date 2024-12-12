package com.example.demo.repository;

import com.example.demo.model.Dificultad;
import com.example.demo.model.Tablero;
import com.example.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TableroRepository extends JpaRepository<Tablero, Long> {

     List<Tablero> findByUsuario(Usuario usuario);

    List<Tablero> findByUsuarioId(Long usuarioId);

    Optional<Tablero> findById(Long id);

    List<Tablero> findByDificultad(Dificultad dificultad);

    List<Tablero> findByUsuarioAndDificultad(Usuario usuario, Dificultad dificultad);

    @Query("SELECT t FROM Tablero t WHERE t.usuario.id = :usuarioId AND t.fechaCreacion BETWEEN :fechaInicio AND :fechaFin")
    List<Tablero> findTablerosPorUsuarioYFecha(@Param("usuarioId") Long usuarioId, @Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin);

    void deleteByUsuario(Usuario usuario);

    void deleteByUsuarioId(Long usuarioId);


}