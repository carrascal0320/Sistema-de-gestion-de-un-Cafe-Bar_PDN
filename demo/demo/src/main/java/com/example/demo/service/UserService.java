package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;
  private static final Logger logger = LoggerFactory.getLogger(UserService.class);

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
    this.passwordEncoder = new BCryptPasswordEncoder();
  }

  //  Buscar usuario por email
  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  //  Buscar usuario por teléfono
  public Optional<User> findByPhone(String phone) {
    return userRepository.findByPhone(phone);
  }

  //  Registrar un usuario con email y contraseña
  public User registerUser(User user) {
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
      throw new IllegalArgumentException("Email already in use");
    }

    if (userRepository.findByPhone(user.getPhone()).isPresent()) {
      throw new IllegalArgumentException("Phone number already in use");
    }

    // Encriptar la contraseña antes de guardarla en la BD
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    return userRepository.save(user);
  }

  //  Autenticación de usuario con email y contraseña
  public boolean authenticate(String email, String password) {
    Optional<User> userOptional = userRepository.findByEmail(email);

    if (userOptional.isPresent()) {
      User user = userOptional.get();
      return passwordEncoder.matches(password, user.getPassword()); // Compara la contraseña encriptada
    }

    return false;
  }

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
}
