package com.example.demo.repository;

import com.example.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Métodos heredados de JpaRepository
    Optional<Usuario> findById(Long id);
    List<Usuario> findAll();
    List<Usuario> findAllById(Iterable<Long> ids);
    <S extends Usuario> S save(S entity);
    <S extends Usuario> List<S> saveAll(Iterable<S> entities);
    void deleteById(Long id);
    void delete(Usuario entity);
    void deleteAllById(Iterable<? extends Long> ids);
    void deleteAll(Iterable<? extends Usuario> entities);
    void deleteAll();
    long count();
    boolean existsById(Long id);

    // Métodos personalizados
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByNombreUsuarioContaining(String parteDelNombre);
    List<Usuario> findByEmailContaining(String parteDelEmail);

    @Query("SELECT u FROM Usuario u WHERE u.fechaRegistro BETWEEN :fechaInicio AND :fechaFin")
    List<Usuario> findUsuariosPorRangoDeFechas(@Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin);

    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.activo = true")
    Long countUsuariosActivos();

    List<Usuario> findByActivoTrue();
    List<Usuario> findByActivoFalse();

    List<Usuario> findAllByOrderByNombreUsuarioAsc();

    boolean existsByNombreUsuario(String nombreUsuario);
    boolean existsByEmail(String email);

    void deleteByNombreUsuario(String nombreUsuario);

}