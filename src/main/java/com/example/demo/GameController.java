package com.example.demo;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = "*")
public class GameController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/finish")
    public Map<String, Object> finishGame(@RequestBody Map<String, Object> body) {
        String username = (String) body.get("username");
        Integer finalScore = (Integer) body.get("finalScore");
        Boolean won = (Boolean) body.get("won");

        User u = userRepository.findByUsername(username);
        Map<String, Object> response = new HashMap<>();
        if (u == null) {
            response.put("error", "Usuario no encontrado");
            return response;
        }

        int newScore = (u.getPuntaje() == null ? 0 : u.getPuntaje()) + finalScore;
        u.setPuntaje(newScore);

        if (Boolean.TRUE.equals(won)) {
            int newPartidas = (u.getPartidasGanadas() == null ? 0 : u.getPartidasGanadas()) + 1;
            u.setPartidasGanadas(newPartidas);
        }

        userRepository.save(u);

        response.put("newScore", u.getPuntaje());
        response.put("partidasGanadas", u.getPartidasGanadas());
        return response;
    }
}
