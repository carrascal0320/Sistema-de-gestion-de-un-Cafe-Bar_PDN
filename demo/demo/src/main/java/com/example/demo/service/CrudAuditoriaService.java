package com.example.demo.service;

import com.example.demo.entity.CrudAuditoria;
import com.example.demo.repository.CrudAuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort; // Importar para ordenamiento
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Servicio encargado de registrar y consultar los eventos de autenticación de los usuarios.
 */
@Service
public class CrudAuditoriaService {

  @Autowired
  private CrudAuditoriaRepository crudAuditoriaRepository;

  /**
   * Registra un evento de autenticación exitosa en la tabla de auditoría.
   *
   * @param email El email del usuario que se autenticó.
   * @param metodoAutenticacion El método de autenticación utilizado (ej. "PERSONALIZADO", "GOOGLE", "FACEBOOK", "GITHUB").
   */
  public void registrarAutenticacion(String email, String metodoAutenticacion) {
    CrudAuditoria auditoria = new CrudAuditoria(email, metodoAutenticacion);
    crudAuditoriaRepository.save(auditoria);
    System.out.println("✅ Evento de autenticación registrado: Email=" + email + ", Método=" + metodoAutenticacion);
  }

  /**
   * Obtiene todos los registros de auditoría sin filtrar.
   * Puede ser ordenado por un campo específico.
   * @param sortBy Campo por el cual ordenar (ej. "fechaRegistroServidor", "email").
   * @param sortDirection Dirección del ordenamiento ("asc" o "desc").
   * @return Una lista de todos los objetos CrudAuditoria.
   */
  public List<CrudAuditoria> findAllEntries(String sortBy, String sortDirection) {
    Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
    return crudAuditoriaRepository.findAll(sort);
  }

  /**
   * Obtiene todos los registros de auditoría para una fecha específica.
   * @param date La fecha a buscar (ej. "2025-06-07").
   * @param sortBy Campo por el cual ordenar.
   * @param sortDirection Dirección del ordenamiento.
   * @return Una lista de registros de auditoría para la fecha dada.
   */
  public List<CrudAuditoria> findEntriesByDate(LocalDate date, String sortBy, String sortDirection) {
    LocalDateTime startOfDay = date.atStartOfDay(); // 00:00:00 del día
    LocalDateTime endOfDay = date.atTime(LocalTime.MAX); // 23:59:59.999999999 del día
    Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
    // Nota: JpaRepository no soporta directamente Sort con findBy...Between,
    // podrías necesitar un método personalizado o usar Pageable.
    // Para este ejemplo, haremos el findAll y luego filtramos/ordenamos en memoria
    // O una JPQL query si el rendimiento es crítico para grandes volúmenes.
    // Una mejor forma sería:
    // return crudAuditoriaRepository.findByFechaRegistroServidorBetween(startOfDay, endOfDay, sort);
    // Pero para simplicidad y si el repositorio no lo soporta de forma nativa:
    List<CrudAuditoria> allEntries = crudAuditoriaRepository.findByFechaRegistroServidorBetween(startOfDay, endOfDay);
    // Ordenamiento en memoria (no ideal para grandes datasets, pero funcional)
    return allEntries.stream()
      .sorted((e1, e2) -> {
        if ("email".equalsIgnoreCase(sortBy)) {
          return "asc".equalsIgnoreCase(sortDirection) ? e1.getEmail().compareTo(e2.getEmail()) : e2.getEmail().compareTo(e1.getEmail());
        } else { // Default: fechaRegistroServidor
          return "asc".equalsIgnoreCase(sortDirection) ? e1.getFechaRegistroServidor().compareTo(e2.getFechaRegistroServidor()) : e2.getFechaRegistroServidor().compareTo(e1.getFechaRegistroServidor());
        }
      })
      .collect(java.util.stream.Collectors.toList());
  }

  /**
   * Obtiene todo el historial de accesos para un email específico.
   * @param email El email o parte del email a buscar.
   * @param sortBy Campo por el cual ordenar.
   * @param sortDirection Dirección del ordenamiento.
   * @return Una lista de registros de auditoría para el email dado.
   */
  public List<CrudAuditoria> findEntriesByEmail(String email, String sortBy, String sortDirection) {
    Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
    // Nuevamente, para JpaRepository puro:
    // List<CrudAuditoria> entries = crudAuditoriaRepository.findByEmailContainingIgnoreCase(email, sort);
    List<CrudAuditoria> allEntries = crudAuditoriaRepository.findByEmailContainingIgnoreCase(email);
    // Ordenamiento en memoria
    return allEntries.stream()
      .sorted((e1, e2) -> {
        if ("email".equalsIgnoreCase(sortBy)) {
          return "asc".equalsIgnoreCase(sortDirection) ? e1.getEmail().compareTo(e2.getEmail()) : e2.getEmail().compareTo(e1.getEmail());
        } else { // Default: fechaRegistroServidor
          return "asc".equalsIgnoreCase(sortDirection) ? e1.getFechaRegistroServidor().compareTo(e2.getFechaRegistroServidor()) : e2.getFechaRegistroServidor().compareTo(e1.getFechaRegistroServidor());
        }
      })
      .collect(java.util.stream.Collectors.toList());
  }

  /**
   * Obtiene el acceso de un usuario específico en una fecha específica.
   * @param email El email exacto del usuario.
   * @param date La fecha a buscar.
   * @param sortBy Campo por el cual ordenar.
   * @param sortDirection Dirección del ordenamiento.
   * @return Una lista de registros de auditoría para el email y la fecha dados.
   */
  public List<CrudAuditoria> findEntryByEmailAndDate(String email, LocalDate date, String sortBy, String sortDirection) {
    LocalDateTime startOfDay = date.atStartOfDay();
    LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
    Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
    // Para JpaRepository puro:
    // List<CrudAuditoria> entries = crudAuditoriaRepository.findByEmailAndFechaRegistroServidorBetween(email, startOfDay, endOfDay, sort);
    List<CrudAuditoria> allEntries = crudAuditoriaRepository.findByEmailAndFechaRegistroServidorBetween(email, startOfDay, endOfDay);
    // Ordenamiento en memoria
    return allEntries.stream()
      .sorted((e1, e2) -> {
        if ("email".equalsIgnoreCase(sortBy)) {
          return "asc".equalsIgnoreCase(sortDirection) ? e1.getEmail().compareTo(e2.getEmail()) : e2.getEmail().compareTo(e1.getEmail());
        } else { // Default: fechaRegistroServidor
          return "asc".equalsIgnoreCase(sortDirection) ? e1.getFechaRegistroServidor().compareTo(e2.getFechaRegistroServidor()) : e2.getFechaRegistroServidor().compareTo(e1.getFechaRegistroServidor());
        }
      })
      .collect(java.util.stream.Collectors.toList());
  }
}

