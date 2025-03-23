package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;

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

  //  Registrar un usuario con validación de email y phone únicos
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

  //  Autenticación de usuario (verifica email y password)
  public boolean authenticate(String email, String password) {
    Optional<User> userOptional = userRepository.findByEmail(email);

    if (userOptional.isPresent()) {
      User user = userOptional.get();
      return passwordEncoder.matches(password, user.getPassword()); // Compara la contraseña encriptada
    }

    return false;
  }
}
