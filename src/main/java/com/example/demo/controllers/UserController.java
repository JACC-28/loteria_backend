package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:5173") // Permite el origen del frontend
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
}
