package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Clase de configuración general para la aplicación.
 * Define beans compartidos que no están directamente ligados a la configuración de seguridad.
 */
@Configuration
public class AppConfig {

  /**
   * Define el PasswordEncoder a utilizar en la aplicación.
   * BCryptPasswordEncoder es una buena opción para encriptar contraseñas de forma segura.
   *
   * @return Una instancia de BCryptPasswordEncoder.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
