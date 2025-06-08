package com.example.demo.service;

import com.example.demo.entity.CrudAuditoria;
import com.example.demo.repository.CrudAuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
   * @param name El nombre del usuario (puede ser null).
   * @param phone El teléfono del usuario (puede ser null).
   * @param metodoAutenticacion El método de autenticación utilizado (ej. "PERSONALIZADO", "GOOGLE", "FACEBOOK", "GITHUB").
   */
  public void registrarAutenticacion(String email, String name, String phone, String metodoAutenticacion) {
    CrudAuditoria auditoria = new CrudAuditoria(email, name, phone, metodoAutenticacion);
    crudAuditoriaRepository.save(auditoria);
    System.out.println("✅ Evento de autenticación registrado: Email=" + email + ", Nombre=" + name + ", Teléfono=" + phone + ", Método=" + metodoAutenticacion);
  }

  /**
   * Obtiene todos los registros de auditoría sin filtrar.
   * Puede ser ordenado por un campo específico.
   * @param sortBy Campo por el cual ordenar (ej. "fechaRegistroServidor", "email", "name").
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
    LocalDateTime startOfDay = date.atStartOfDay();
    LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
    List<CrudAuditoria> allEntries = crudAuditoriaRepository.findByFechaRegistroServidorBetween(startOfDay, endOfDay);
    return allEntries.stream()
      .sorted((e1, e2) -> {
        if ("email".equalsIgnoreCase(sortBy)) {
          return "asc".equalsIgnoreCase(sortDirection) ? e1.getEmail().compareTo(e2.getEmail()) : e2.getEmail().compareTo(e1.getEmail());
        } else if ("name".equalsIgnoreCase(sortBy)) { // Ordenar por nombre
          return "asc".equalsIgnoreCase(sortDirection) ? compareStrings(e1.getName(), e2.getName()) : compareStrings(e2.getName(), e1.getName());
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
    List<CrudAuditoria> allEntries = crudAuditoriaRepository.findByEmailContainingIgnoreCase(email);
    return allEntries.stream()
      .sorted((e1, e2) -> {
        if ("email".equalsIgnoreCase(sortBy)) {
          return "asc".equalsIgnoreCase(sortDirection) ? e1.getEmail().compareTo(e2.getEmail()) : e2.getEmail().compareTo(e1.getEmail());
        } else if ("name".equalsIgnoreCase(sortBy)) { // Ordenar por nombre
          return "asc".equalsIgnoreCase(sortDirection) ? compareStrings(e1.getName(), e2.getName()) : compareStrings(e2.getName(), e1.getName());
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
    List<CrudAuditoria> allEntries = crudAuditoriaRepository.findByEmailAndFechaRegistroServidorBetween(email, startOfDay, endOfDay);
    return allEntries.stream()
      .sorted((e1, e2) -> {
        if ("email".equalsIgnoreCase(sortBy)) {
          return "asc".equalsIgnoreCase(sortDirection) ? e1.getEmail().compareTo(e2.getEmail()) : e2.getEmail().compareTo(e1.getEmail());
        } else if ("name".equalsIgnoreCase(sortBy)) { // Ordenar por nombre
          return "asc".equalsIgnoreCase(sortDirection) ? compareStrings(e1.getName(), e2.getName()) : compareStrings(e2.getName(), e1.getName());
        } else { // Default: fechaRegistroServidor
          return "asc".equalsIgnoreCase(sortDirection) ? e1.getFechaRegistroServidor().compareTo(e2.getFechaRegistroServidor()) : e2.getFechaRegistroServidor().compareTo(e1.getFechaRegistroServidor());
        }
      })
      .collect(java.util.stream.Collectors.toList());
  }

  /**
   * Método auxiliar para comparar cadenas que pueden ser nulas.
   */
  private int compareStrings(String s1, String s2) {
    if (s1 == null && s2 == null) {
      return 0;
    }
    if (s1 == null) {
      return -1; // null viene antes
    }
    if (s2 == null) {
      return 1; // null viene antes
    }
    return s1.compareTo(s2);
  }
}
