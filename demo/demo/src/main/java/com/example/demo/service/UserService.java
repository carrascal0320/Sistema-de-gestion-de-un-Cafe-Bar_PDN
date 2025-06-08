package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CrudAuditoriaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final CrudAuditoriaService crudAuditoriaService;

  private static final Logger logger = LoggerFactory.getLogger(UserService.class);

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CrudAuditoriaService crudAuditoriaService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.crudAuditoriaService = crudAuditoriaService;
  }

  // Buscar usuario por email
  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  // Buscar usuario por teléfono
  public Optional<User> findByPhone(String phone) {
    return userRepository.findByPhone(phone);
  }

  // Registrar un usuario con email y contraseña
  public User registerUser(User user) {
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
      throw new IllegalArgumentException("Email already in use");
    }

    if (userRepository.findByPhone(user.getPhone()).isPresent()) {
      throw new IllegalArgumentException("Phone number already in use");
    }

    user.setPassword(passwordEncoder.encode(user.getPassword()));

    User newUser = userRepository.save(user);
    return newUser;
  }

  // Autenticación de usuario con email y contraseña
  public boolean authenticate(String email, String password) {
    Optional<User> userOptional = userRepository.findByEmail(email);

    if (userOptional.isPresent()) {
      User user = userOptional.get();
      if (passwordEncoder.matches(password, user.getPassword())) {
        crudAuditoriaService.registrarAutenticacion(email, "PERSONALIZADO");
        return true;
      }
    }
    return false;
  }

  /**
   * Procesa un usuario que se autentica a través de OAuth (Google, GitHub, Facebook).
   * Este método registra el usuario si no existe, o lo recupera si ya está en la DB.
   * La auditoría del *evento* de autenticación OAuth debe hacerse en el controlador OAuth,
   * después de que este método haya asegurado que el usuario está en la DB.
   */
  public User processOAuthUser(String email, String name) {
    Optional<User> existingUser = userRepository.findByEmail(email);

    if (existingUser.isPresent()) {
      logger.info("✅ Usuario OAuth ya registrado en la BD: " + email);
      return existingUser.get();
    }

    logger.info("🆕 Registrando nuevo usuario OAuth: " + email);
    User newUser = new User(email, name);
    User savedUser = userRepository.save(newUser);
    logger.info("✅ Usuario guardado en BD con ID: " + savedUser.getId());

    return savedUser;
  }

  public void resetPassword(String email, String newPassword) {
    Optional<User> optionalUser = userRepository.findByEmail(email);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      user.setPassword(passwordEncoder.encode(newPassword));
      userRepository.save(user);
      logger.info("🔐 Contraseña actualizada para el usuario: " + email);
    } else {
      throw new IllegalArgumentException("Usuario no encontrado con el correo: " + email);
    }
  }

  public void save(User user) {
    userRepository.save(user);
  }
}
