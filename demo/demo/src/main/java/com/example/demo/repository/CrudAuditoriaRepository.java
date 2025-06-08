package com.example.demo.repository;

import com.example.demo.entity.CrudAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio JPA para la entidad CrudAuditoria.
 * Provee métodos CRUD básicos y métodos de consulta personalizados.
 */
@Repository
public interface CrudAuditoriaRepository extends JpaRepository<CrudAuditoria, Long> {

  /**
   * Busca todos los registros de auditoría para un email específico.
   * Permite búsquedas parciales e insensibles a mayúsculas/minúsculas.
   * @param email El email o parte del email a buscar.
   * @return Una lista de registros de auditoría que coinciden con el email.
   */
  List<CrudAuditoria> findByEmailContainingIgnoreCase(String email);

  /**
   * Busca todos los registros de auditoría en un rango de fechas.
   * Esto es útil para buscar entradas dentro de un día específico (de inicio a fin del día).
   * @param startOfDay La fecha y hora de inicio del rango (ej. 2025-06-07 00:00:00).
   * @param endOfDay La fecha y hora de fin del rango (ej. 2025-06-07 23:59:59).
   * @return Una lista de registros de auditoría dentro del rango de fechas.
   */
  List<CrudAuditoria> findByFechaRegistroServidorBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

  /**
   * Busca registros de auditoría para un email específico dentro de un rango de fechas.
   * @param email El email exacto del usuario.
   * @param startOfDay La fecha y hora de inicio del rango.
   * @param endOfDay La fecha y hora de fin del rango.
   * @return Una lista de registros de auditoría que coinciden con el email y el rango de fechas.
   */
  List<CrudAuditoria> findByEmailAndFechaRegistroServidorBetween(String email, LocalDateTime startOfDay, LocalDateTime endOfDay);

}
