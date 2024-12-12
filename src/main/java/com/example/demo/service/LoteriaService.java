package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.EstadisticaRepository;
import com.example.demo.repository.NumeroRepository;
import com.example.demo.repository.TableroRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import com.example.demo.exception.*;
import java.util.List;
import com.example.demo.model.Numero;
import java.util.*;
import java.util.stream.Collectors;
import com.example.demo.model.Dificultad;

@Service
public class LoteriaService {

    private final UsuarioRepository usuarioRepository;
    private final TableroRepository tableroRepository;
    private final NumeroRepository numeroRepository;
    private final EstadisticaRepository estadisticaRepository;

    public LoteriaService(UsuarioRepository usuarioRepository, TableroRepository tableroRepository, NumeroRepository numeroRepository, EstadisticaRepository estadisticaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.tableroRepository = tableroRepository;
        this.numeroRepository = numeroRepository;
        this.estadisticaRepository = estadisticaRepository;
    }

    public List<NumeroGanador> generarNumerosGanadores() {
        Set<Integer> numeros = new HashSet<>();
        Random random = new Random();

        while (numeros.size() < 9) {
            int numero = random.nextInt(54) + 1;
            numeros.add(numero);
        }

        return numeros.stream()
                .map(NumeroGanador::new) // Usa el constructor de NumeroGanador
                .collect(Collectors.toList());
    }


    public Tablero crearTablero(Long usuarioId, Dificultad dificultad) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        Tablero tablero = new Tablero();
        tablero.setUsuario(usuario);
        tablero.setDificultad(dificultad);

        List<Numero> numerosTablero = generarNumerosParaTablero(dificultad);
        tablero.setNumeros(numerosTablero);

        return tableroRepository.save(tablero);
    }

    private List<Numero> generarNumerosParaTablero(Dificultad dificultad) {
        int numNumeros = 9; // Puedes ajustar este valor según la dificultad si lo deseas
        Set<Integer> numeros = new HashSet<>();
        Random random = new Random();

        while (numeros.size() < numNumeros) {
            int numero = random.nextInt(54) + 1;
            numeros.add(numero);
        }

        return numeros.stream()
                .map(numero -> {
                    Numero nuevoNumero = new Numero();
                    nuevoNumero.setValor(numero);
                    return numeroRepository.save(nuevoNumero);
                })
                .collect(Collectors.toList());
    }

    public boolean verificarGanador(Tablero tablero, List<NumeroGanador> numerosGanadores) {
        return tablero.getNumeros().stream()
                .map(Numero::getValor) // Ahora es una referencia de método de instancia
                .allMatch(valorTablero -> numerosGanadores.stream()
                        .anyMatch(numeroGanador -> numeroGanador.getValor().equals(valorTablero)));
    }

    public Estadistica obtenerEstadisticasUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + usuarioId));

        return estadisticaRepository.findByUsuario(usuario)
                .orElseGet(() -> {
                    Estadistica nuevaEstadistica = new Estadistica();
                    nuevaEstadistica.setUsuario(usuario);
                    nuevaEstadistica.setPartidasJugadas(0);
                    nuevaEstadistica.setPartidasGanadas(0);
                    nuevaEstadistica.setBoletosComprados(0);
                    return estadisticaRepository.save(nuevaEstadistica);
                });

    }

    public void actualizarEstadisticasPartida(Usuario usuario, boolean gano) {
        Estadistica estadistica = obtenerEstadisticasUsuario(usuario.getId());
        estadistica.setPartidasJugadas(estadistica.getPartidasJugadas() + 1);
        if(gano){
            estadistica.setPartidasGanadas(estadistica.getPartidasGanadas()+1);
        }
        estadisticaRepository.save(estadistica);
    }

    public void actualizarEstadisticasBoletosComprados(Usuario usuario, int cantidadBoletos){
        Estadistica estadistica = obtenerEstadisticasUsuario(usuario.getId());
        estadistica.setBoletosComprados(estadistica.getBoletosComprados() + cantidadBoletos);
        estadisticaRepository.save(estadistica);
    }

    public Boleto comprarBoleto(Usuario usuario, Dificultad dificultad) {
        Tablero tablero = new Tablero();
        tablero.setUsuario(usuario);
        tablero.setDificultad(dificultad);
        tablero.setNumeros(generarNumerosParaTablero(dificultad));

        Tablero tableroGuardado = tableroRepository.save(tablero);

        Boleto boleto = new Boleto();
        boleto.setTablero(tableroGuardado);
        return boleto;
    }

}