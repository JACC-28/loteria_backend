package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173") // Configuración CORS
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User newUser) {
        // Verificar si el usuario ya existe
        if (userRepository.findByUser(newUser.getUser()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El usuario ya existe");
        }

        // Crear y guardar el nuevo usuario
        newUser.setScore(0); // Inicializar la puntuación
        newUser.setDate(LocalDate.now()); // Establecer la fecha de creación
        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario creado exitosamente");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam("user") String username, @RequestParam("password") String password) {
        User user = userRepository.findByUser(username);

        if (user != null && user.getPassword().equals(password)) {
            // Incluir el rol al generar el token
            String token = JwtUtil.generateToken(user.getUser(), user.getRole());
            return ResponseEntity.ok(token);
        } else {
            // Retornar un estado 401 para credenciales incorrectas
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }
    }

    @GetMapping("/user")
    public ResponseEntity<String> getUserName(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing or invalid Authorization header");
        }

        try {
            String token = authorizationHeader.substring(7);
            String username = JwtUtil.extractUsername(token);
            String role = JwtUtil.extractRole(token); // Extraer el rol del token

            return ResponseEntity.ok("Hello, " + username + "! Your role is: " + role);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    // Nuevo endpoint: Obtener usuarios ordenados por puntuación
    @GetMapping("/users/scoreboard")
    public ResponseEntity<List<User>> getUsersByScore() {
        List<User> users = userRepository.findAll();
        users.sort((u1, u2) -> Integer.compare(u2.getScore(), u1.getScore())); // Ordenar por puntuación descendente
        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/update-score/{username}")
    public ResponseEntity<String> updateScore(
            @PathVariable String username,
            @RequestBody Map<String, Integer> requestBody) { // Cambiar para leer desde el cuerpo
        int score = requestBody.get("score");
        User user = userRepository.findByUser(username);

        if (user != null) {
            user.setScore(user.getScore() + score); // Sumar la nueva puntuación
            user.setDate(LocalDate.now()); // Actualizar la fecha
            userRepository.save(user);
            return ResponseEntity.ok("Puntuación actualizada correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }



    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

}
