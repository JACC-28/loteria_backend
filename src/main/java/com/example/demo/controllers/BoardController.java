package com.example.demo.controllers;

import com.example.demo.models.Card;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/boards")
@CrossOrigin(origins = "http://localhost:5173")
public class BoardController {

    private final Map<Long, List<Card>> savedBoards = new HashMap<>();

    private long boardIdCounter = 1;

    @PostMapping
    public ResponseEntity<List<Long>> saveBoards(@RequestBody Map<String, List<List<Card>>> body) {
        List<List<Card>> boards = body.get("tableros"); // Extraer los tableros
        if (boards == null) {
            System.out.println("Formato inválido: no se encontraron tableros en el cuerpo de la solicitud.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Validar el formato
        }

        List<Long> boardIds = new ArrayList<>(); // Lista para almacenar los IDs generados

        for (List<Card> board : boards) {
            long boardId = boardIdCounter++; // Generar un ID único para el tablero
            savedBoards.put(boardId, board); // Guardar el tablero individualmente
            boardIds.add(boardId); // Agregar el ID a la lista

            // Registro para verificar qué datos se están guardando
            System.out.println("Tablero guardado con ID " + boardId + ": " + board);
        }

        System.out.println("Todos los tableros en memoria después de guardar: " + savedBoards);
        return ResponseEntity.ok(boardIds); // Retornar todos los IDs generados
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Card>> getBoard(@PathVariable Long id) {
        if (savedBoards.containsKey(id)) {
            List<Card> board = savedBoards.get(id);
            System.out.println("Tablero recuperado con ID " + id + ": " + board);
            return ResponseEntity.ok(board); // Retornar el tablero correspondiente al ID
        }
        System.out.println("Tablero no encontrado con ID " + id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Retornar 404 si no existe el ID
    }

    @GetMapping
    public ResponseEntity<List<Long>> getAllBoardIds() {
        List<Long> boardIds = new ArrayList<>(savedBoards.keySet());
        System.out.println("IDs de tableros disponibles: " + boardIds);
        return ResponseEntity.ok(boardIds); // Retornar todos los IDs generados
    }
}
