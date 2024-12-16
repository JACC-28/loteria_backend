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
        newUser.setGanadas(0); // Inicializar las ganadas
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

    // Obtener usuarios ordenados por puntuación
    @GetMapping("/users/scoreboard")
    public ResponseEntity<List<User>> getUsersByScore() {
        List<User> users = userRepository.findAll();
        users.sort((u1, u2) -> Integer.compare(u2.getScore(), u1.getScore())); // Ordenar por puntuación descendente
        return ResponseEntity.ok(users);
    }

    // Actualizar el score del usuario sumando un valor
    @PutMapping("/users/update-score/{username}")
    public ResponseEntity<String> updateScore(
            @PathVariable String username,
            @RequestBody Map<String, Integer> requestBody) {

        Integer scoreToAdd = requestBody.get("score");
        if (scoreToAdd == null) {
            return ResponseEntity.badRequest().body("No se envió la puntuación a sumar");
        }

        User user = userRepository.findByUser(username);

        if (user != null) {
            user.setScore(user.getScore() + scoreToAdd); // Sumar la nueva puntuación
            user.setDate(LocalDate.now()); // Actualizar la fecha
            userRepository.save(user);
            return ResponseEntity.ok("Puntuación actualizada correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }

    // Actualizar las ganadas del usuario sumando 1
    @PutMapping("/users/update-ganadas/{username}")
    public ResponseEntity<String> updateGanadas(@PathVariable String username) {
        User user = userRepository.findByUser(username);

        if (user != null) {
            user.setGanadas(user.getGanadas() + 1);
            userRepository.save(user);
            return ResponseEntity.ok("Ganadas incrementadas correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }

    // Obtener todos los usuarios
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    // Obtener un usuario por su username
    @GetMapping("/users/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        User user = userRepository.findByUser(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
        return ResponseEntity.ok(user);
    }

    // Actualizar información del usuario (ej. password, role)
    @PutMapping("/users/{username}")
    public ResponseEntity<?> updateUser(@PathVariable String username, @RequestBody User updatedUser) {
        User user = userRepository.findByUser(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        // Actualizar campos necesarios:
        // Aquí solo como ejemplo, password y role (score y ganadas se actualizan con los endpoints dedicados)
        if (updatedUser.getPassword() != null) {
            user.setPassword(updatedUser.getPassword());
        }
        if (updatedUser.getRole() != null) {
            user.setRole(updatedUser.getRole());
        }

        userRepository.save(user);
        return ResponseEntity.ok("Usuario actualizado correctamente");
    }

    // Eliminar un usuario
    @DeleteMapping("/users/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        User user = userRepository.findByUser(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        userRepository.delete(user);
        return ResponseEntity.ok("Usuario eliminado correctamente");
    }

}
