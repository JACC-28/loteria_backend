package com.example.demo.controllers;

import com.example.demo.models.Card;
import com.example.demo.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cards")
@CrossOrigin(origins = "http://localhost:5173") // Permitir solicitudes desde el frontend
public class CardController {

    @Autowired
    private CardRepository cardRepository;

    // Directorio de subida configurado en application.properties
    @Value("${file.upload-dir}")
    private String uploadDir;

    // Obtener todas las cartas
    @GetMapping
    public ResponseEntity<List<Card>> getAllCards() {
        return ResponseEntity.ok(cardRepository.findAll());
    }

    // Añadir una nueva carta
    @PostMapping
    public ResponseEntity<Card> addCard(@RequestBody Card newCard) {
        Card savedCard = cardRepository.save(newCard);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCard);
    }

    // Subir una imagen
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // Usar la ruta configurada en application.properties
            String directory = uploadDir;

            // Validar el tipo de archivo
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Solo se permiten archivos de imagen");
            }

            // Generar un nombre único para evitar conflictos
            String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            // Crear el archivo en el directorio configurado
            File dest = new File(directory + uniqueFilename);

            // Crear el directorio si no existe
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }

            // Guardar el archivo
            file.transferTo(dest);

            // Retornar la URL relativa de la imagen
            String imagePath = "/imagenes/" + uniqueFilename;
            System.out.println("Archivo guardado con éxito en: " + dest.getAbsolutePath());
            return ResponseEntity.ok(imagePath);

        } catch (IOException e) {
            System.err.println("Error al subir la imagen: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir la imagen: " + e.getMessage());
        }
    }



    // Eliminar una carta por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        if (cardRepository.existsById(id)) {
            cardRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
