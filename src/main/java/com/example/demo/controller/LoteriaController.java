package com.example.demo.controller;

import com.example.demo.exception.UsuarioNoEncontradoException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.LoteriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.exception.TableroNoEncontradoException;
import java.util.List;

@RestController
@RequestMapping("/api/loteria")
public class LoteriaController {

    @Autowired
    private LoteriaService loteriaService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TableroRepository tableroRepository;

    @PostMapping("/boletos")
    public ResponseEntity<Boleto> comprarBoleto(@RequestBody Usuario usuario, @RequestParam Dificultad dificultad) {
        Boleto boleto = loteriaService.comprarBoleto(usuario, dificultad);
        return ResponseEntity.status(HttpStatus.CREATED).body(boleto);
    }

    @GetMapping("/verificar-ganador/{tableroId}")
    public ResponseEntity<Boolean> verificarGanador(@PathVariable Long tableroId, @RequestBody List<NumeroGanador> numerosGanadores) {
        Tablero tablero = tableroRepository.findById(tableroId) // Utiliza la instancia inyectada
                .orElseThrow(() -> new TableroNoEncontradoException("Tablero no encontrado"));
        boolean esGanador = loteriaService.verificarGanador(tablero, numerosGanadores);
        return ResponseEntity.ok(esGanador);
    }

    @GetMapping("/historial/{usuarioId}")
    public ResponseEntity<List<Tablero>> obtenerHistorialUsuario(@PathVariable Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));
        List<Tablero> historial = tableroRepository.findByUsuario(usuario);
        return ResponseEntity.ok(historial);
    }

}