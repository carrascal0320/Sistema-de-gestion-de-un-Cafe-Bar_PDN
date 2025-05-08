package com.example.demo.config;

import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;

@Configuration
public class SecurityConfig {

  private final UserService userService;

  public SecurityConfig(UserService userService) {
    this.userService = userService;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())  // Desactiva CSRF (si es necesario en tu contexto)
      .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // Habilita CORS
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/auth/register", "/api/auth/login", "/api/auth/user", "/api/auth/reset-password", "/api/auth/request-reset")
        .permitAll()
        .anyRequest().authenticated()
      )
      .oauth2Login(oauth2 -> oauth2
        .successHandler(oAuthSuccessHandler())  // Maneja el éxito de OAuth2
        .failureUrl("http://localhost:4200/login?error=true")
      )
      .logout(logout -> logout
        .logoutUrl("/api/auth/logout")
        .logoutSuccessHandler((request, response, authentication) -> {
          response.setStatus(HttpServletResponse.SC_OK);
          response.sendRedirect("http://localhost:4200/login");
        })
      );

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:4200"));  // Permite solicitudes desde el frontend
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept"));  // Asegúrate de incluir los encabezados necesarios
    configuration.setAllowCredentials(true);  // Permite enviar credenciales (cookies, headers, etc.)

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationSuccessHandler oAuthSuccessHandler() {
    return (request, response, authentication) -> {
      OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
      String email = oAuth2User.getAttribute("email");
      String name = oAuth2User.getAttribute("name");

      if (email == null) {
        email = oAuth2User.getAttribute("login");
        if (email == null && oAuth2User.getAttribute("id") != null) {
          email = oAuth2User.getAttribute("id") + "@facebook.com";
        }
      }
      userService.processOAuthUser(email, name);

      response.sendRedirect("http://localhost:4200/dashboard?email=" + email + "&name=" + name);
    };
  }
}
