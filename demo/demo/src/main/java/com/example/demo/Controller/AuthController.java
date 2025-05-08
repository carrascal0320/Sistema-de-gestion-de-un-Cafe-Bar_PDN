package com.example.demo.Controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import com.example.demo.entity.PasswordResetToken;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {
  private final UserService userService;

  @Autowired
  private PasswordResetService passwordResetService;

  @Autowired
  private EmailService emailService;

  @Autowired
  private UserRepository userRepository;

  @PostMapping("/request-reset")
  public ResponseEntity<?> requestReset(@RequestBody Map<String, String> request) {
    String email = request.get("email");
    Optional<User> optionalUser = userService.findByEmail(email);

    if (optionalUser.isEmpty()) {
      return ResponseEntity.badRequest().body(Map.of("message", "Correo no encontrado"));
    }


    String resetLink = "http://localhost:4200/reset-password";

    emailService.sendEmail(email, "Restablecer Contraseña",
      "Haz clic en este enlace para restablecer tu contraseña: " + resetLink);

    return ResponseEntity.ok(Map.of("message", "Correo de restablecimiento enviado"));
  }

  @PostMapping("/reset-password")
  public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
    String email = request.get("email");
    String newPassword = request.get("password");

    try {
      userService.resetPassword(email, newPassword); // Llamamos al método del servicio
      return ResponseEntity.ok(Map.of("message", "Contraseña actualizada correctamente"));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }
  }


  public AuthController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody User user) {
    if (user.getEmail() == null || user.getPassword() == null || user.getName() == null || user.getPhone() == null ||
      user.getEmail().isBlank() || user.getPassword().isBlank() || user.getName().isBlank() || user.getPhone().isBlank()) {
      return ResponseEntity.badRequest().body(Map.of("message", "All fields are required"));
    }

    if (userService.findByEmail(user.getEmail()).isPresent()) {
      return ResponseEntity.badRequest().body(Map.of("message", "Email already in use"));
    }

    if (userService.findByPhone(user.getPhone()).isPresent()) {
      return ResponseEntity.badRequest().body(Map.of("message", "Phone already in use"));
    }

    try {
      User savedUser = userService.registerUser(user);
      return ResponseEntity.ok(Map.of("message", "User registered successfully", "userId", savedUser.getId()));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(500).body(Map.of("message", "Error during registration", "error", e.getMessage()));
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
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

  //  Proceso para usuarios con Google/GitHub
  @GetMapping("/user")
  public ResponseEntity<?> getAuthenticatedUser(Principal principal) {
    if (principal == null) {
      return ResponseEntity.status(401).body(Map.of("message", "User not authenticated"));
    }

    String email = principal.getName();
    Optional<User> existingUser = userService.findByEmail(email);

    if (existingUser.isPresent()) {
      return ResponseEntity.ok(existingUser.get());
    }


    OAuth2User oAuth2User = (OAuth2User) principal;
    String name = oAuth2User.getAttribute("name");

    User newUser = new User();
    newUser.setEmail(email);
    newUser.setName(name);
    newUser.setPassword("OAuthUser");
    newUser.setPhone("N/A");

    User savedUser = userService.registerUser(newUser);
    return ResponseEntity.ok(savedUser);
  }
}
