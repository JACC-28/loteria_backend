package com.example.demo.controllers;

import com.example.demo.models.Card;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/boards")
public class BoardController {

    private final Map<Long, List<List<Card>>> savedBoards = new HashMap<>();
    private long boardIdCounter = 1;

    // Guardar tableros
    @PostMapping
    public ResponseEntity<Long> saveBoard(@RequestBody List<List<Card>> boards) {
        long boardId = boardIdCounter++;
        savedBoards.put(boardId, boards);
        return ResponseEntity.ok(boardId);
    }

    // Obtener un tablero por ID
    @GetMapping("/{id}")
    public ResponseEntity<List<List<Card>>> getBoard(@PathVariable Long id) {
        if (savedBoards.containsKey(id)) {
            return ResponseEntity.ok(savedBoards.get(id));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
