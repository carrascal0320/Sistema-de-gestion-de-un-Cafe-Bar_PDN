package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa un registro de auditoría de un usuario autenticado.
 * Cada vez que un usuario inicia sesión (ya sea por email/contraseña, Google, GitHub o Facebook),
 * se creará un nuevo registro en esta tabla.
 */
@Entity
@Table(name = "crud_auditoria") // Nombre de tabla simplificado
public class CrudAuditoria { // Nombre de clase simplificado

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String email; // El email del usuario que se autenticó

  @Column(nullable = true) // El nombre del usuario, puede ser nulo
  private String name;

  @Column(nullable = true) // El teléfono del usuario, puede ser nulo
  private String phone;

  @Column(nullable = false)
  private String metodoAutenticacion; // El método usado: "PERSONALIZADO", "GOOGLE", "FACEBOOK", "GITHUB"

  @Column(nullable = false, updatable = false)
  private LocalDateTime fechaRegistroServidor; // Fecha y hora exactas del servidor

  // Constructor vacío requerido por JPA
  public CrudAuditoria() {
  }

  /**
   * Constructor para crear un nuevo registro de auditoría con nombre y teléfono.
   * @param email El email del usuario autenticado.
   * @param name El nombre del usuario (puede ser null).
   * @param phone El teléfono del usuario (puede ser null).
   * @param metodoAutenticacion El método de autenticación utilizado.
   */
  public CrudAuditoria(String email, String name, String phone, String metodoAutenticacion) {
    this.email = email;
    this.name = name;
    this.phone = phone;
    this.metodoAutenticacion = metodoAutenticacion;
  }

  // --- Getters y Setters ---

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getMetodoAutenticacion() {
    return metodoAutenticacion;
  }

  public void setMetodoAutenticacion(String metodoAutenticacion) {
    this.metodoAutenticacion = metodoAutenticacion;
  }

  public LocalDateTime getFechaRegistroServidor() {
    return fechaRegistroServidor;
  }

  public void setFechaRegistroServidor(LocalDateTime fechaRegistroServidor) {
    this.fechaRegistroServidor = fechaRegistroServidor;
  }

  /**
   * Método que se ejecuta automáticamente antes de persistir la entidad.
   * Establece la fecha y hora actuales del servidor para el registro de auditoría.
   */
  @PrePersist
  protected void onCreate() {
    this.fechaRegistroServidor = LocalDateTime.now();
  }
}
