package com.example.demo.repository; // Asegúrate de que este sea el paquete correcto

import com.example.demo.model.Numero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NumeroRepository extends JpaRepository<Numero, Long> {

    Optional<Numero> findByValor(Integer valor);

    // Puedes agregar consultas personalizadas aquí si necesitas buscar números por otros criterios
    // Ejemplo:
    // List<Numero> findByValorGreaterThan(Integer valor);
}