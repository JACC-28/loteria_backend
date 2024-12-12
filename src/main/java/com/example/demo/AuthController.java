package com.example.demo;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        // Validar que username y password no sean vacíos
        if (user.getUsername() == null || user.getUsername().trim().isEmpty() ||
                user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Username y password son obligatorios");
            return response;
        }

        User saved = userService.register(user);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Usuario registrado con éxito");
        return response;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> creds) {
        String username = creds.get("username");
        String password = creds.get("password");
        User u = userService.login(username, password);
        Map<String, Object> response = new HashMap<>();
        if (u != null) {
            response.put("message", "Login exitoso");
            response.put("username", u.getUsername());
            response.put("admin", u.getAdmin()); // Si es "admin" => admin, si null => user normal
            return response;
        } else {
            response.put("error", "Credenciales inválidas");
            return response;
        }
    }
}
