package com.example.demo.Controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*") //
public class AuthController {
  private final UserService userService;

  public AuthController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody User user) {
    //  Validaciones de entrada
    if (user.getEmail() == null || user.getPassword() == null || user.getName() == null || user.getPhone() == null ||
      user.getEmail().isBlank() || user.getPassword().isBlank() || user.getName().isBlank() || user.getPhone().isBlank()) {
      return ResponseEntity.badRequest().body(Map.of("message", "All fields are required (name, email, password, phone)"));
    }

    //  Evita registros duplicados
    if (userService.findByEmail(user.getEmail()).isPresent()) {
      return ResponseEntity.badRequest().body(Map.of("message", "Email already in use"));
    }

    if (userService.findByPhone(user.getPhone()).isPresent()) {
      return ResponseEntity.badRequest().body(Map.of("message", "Phone number already in use"));
    }

    try {
      User savedUser = userService.registerUser(user);
      return ResponseEntity.ok(Map.of("message", "User registered successfully", "userId", savedUser.getId()));
    } catch (IllegalArgumentException e) { // Error esperado
      return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    } catch (Exception e) { // Error inesperado
      return ResponseEntity.status(500).body(Map.of("message", "Error during registration", "error", e.getMessage()));
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
    //  Validaciones de entrada
    if (!credentials.containsKey("email") || !credentials.containsKey("password") ||
      credentials.get("email").isBlank() || credentials.get("password").isBlank()) {
      return ResponseEntity.badRequest().body(Map.of("message", "Email and password are required"));
    }

    boolean authenticated = userService.authenticate(credentials.get("email"), credentials.get("password"));

    if (!authenticated) {
      return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
    }

    return ResponseEntity.ok(Map.of("message", "Login successful"));
  }
}
