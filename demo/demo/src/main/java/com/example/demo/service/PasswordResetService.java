package com.example.demo.service;

import com.example.demo.entity.PasswordResetToken;
import com.example.demo.entity.User;
import com.example.demo.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class PasswordResetService {

  @Autowired
  private PasswordResetTokenRepository passwordResetTokenRepository;

  public String createPasswordResetToken(User user) {
    String token = UUID.randomUUID().toString();
    PasswordResetToken resetToken = new PasswordResetToken();
    resetToken.setToken(token);
    resetToken.setUser(user);
    resetToken.setExpirationDate(new Date(System.currentTimeMillis() + 3600 * 1000));
    passwordResetTokenRepository.save(resetToken);
    return token;
  }

  public PasswordResetToken findByToken(String token) {
    return passwordResetTokenRepository.findByToken(token);
  }

  public void deleteToken(PasswordResetToken token) {
    passwordResetTokenRepository.delete(token);
  }

  public ResponseEntity<String> validateToken(String token) {
    PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);
    if (resetToken == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token no encontrado");
    }

    // Validación de expiración del token
    if (resetToken.getExpirationDate().before(new Date())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El token ha expirado");
    }

    return ResponseEntity.ok("Token válido");
  }
}
