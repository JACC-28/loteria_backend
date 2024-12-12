package com.example.demo;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User register(User user) {
        // Por defecto puntaje = 0, partidasGanadas = 0, admin = null
        user.setPuntaje(0);
        user.setPartidasGanadas(0);
        return userRepository.save(user);
    }

    public User login(String username, String password) {
        User u = userRepository.findByUsername(username);
        if (u != null && u.getPassword().equals(password)) {
            return u;
        }
        return null;
    }

    public List<User> getTopPlayers() {
        return userRepository.findAll().stream()
                .sorted((a,b) -> b.getPuntaje().compareTo(a.getPuntaje()))
                .limit(10)
                .collect(Collectors.toList());
    }
}
