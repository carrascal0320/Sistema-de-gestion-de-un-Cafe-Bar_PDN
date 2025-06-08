package com.example.demo.config;

import com.example.demo.service.UserService;
import com.example.demo.service.CrudAuditoriaService;
import com.example.demo.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.util.List;

@Configuration
public class SecurityConfig {

  private final UserService userService;
  private final CrudAuditoriaService crudAuditoriaService;

  public SecurityConfig(UserService userService, CrudAuditoriaService crudAuditoriaService) {
    this.userService = userService;
    this.crudAuditoriaService = crudAuditoriaService;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .cors(cors -> cors.configurationSource(corsConfigurationSource()))
      .authorizeHttpRequests(auth -> auth
        .requestMatchers(
          "/api/auth/register",
          "/api/auth/login",
          "/api/auth/request-reset",
          "/api/auth/reset-password",
          "/api/users/**"
        ).permitAll()
        .requestMatchers(
          "/api/auth/user",
          "/api/auditoria/**"
        ).authenticated()
        .anyRequest().authenticated())
      .oauth2Login(oauth2 -> oauth2
        .successHandler(oAuthSuccessHandler())
        .failureUrl("http://localhost:4200/login?error=true"))
      .logout(logout -> logout
        .logoutUrl("/api/auth/logout")
        .logoutSuccessHandler((request, response, authentication) -> {
          response.setStatus(HttpServletResponse.SC_OK);
          response.sendRedirect("http://localhost:4200/login");
        }))
      .exceptionHandling(exception -> exception
        .authenticationEntryPoint((request, response, authException) -> {
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          response.setContentType("application/json");
          response.getWriter().write("{\"error\": \"No autorizado\", \"message\": \"" + authException.getMessage() + "\"}");
        }));

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:4200"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public AuthenticationSuccessHandler oAuthSuccessHandler() {
    return (request, response, authentication) -> {
      OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
      String email = oAuth2User.getAttribute("email");
      String name = oAuth2User.getAttribute("name");
      String phone = null; // No hay un atributo estándar 'phone' en OAuth2User por defecto

      if (email == null) {
        email = oAuth2User.getAttribute("login");
      }
      if (email == null && authentication instanceof OAuth2AuthenticationToken) {
        String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        if ("facebook".equals(registrationId) && oAuth2User.getAttribute("id") != null) {
          email = oAuth2User.getAttribute("id") + "@facebook.com";
        }
      }

      String metodoAutenticacion = "DESCONOCIDO";
      if (authentication instanceof OAuth2AuthenticationToken) {
        metodoAutenticacion = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId().toUpperCase();
      }

      if (email != null) {
        User userInDb = userService.processOAuthUser(email, name);
        if (userInDb != null && userInDb.getPhone() != null) {
          phone = userInDb.getPhone();
        }

        crudAuditoriaService.registrarAutenticacion(email, name, phone, metodoAutenticacion);
        response.sendRedirect("http://localhost:4200/dashboard?email=" + email + "&name=" + (name != null ? name : ""));
      } else {
        response.sendRedirect("http://localhost:4200/login?error=email_not_found");
      }
    };
  }
}
