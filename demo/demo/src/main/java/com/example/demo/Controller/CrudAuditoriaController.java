package com.example.demo.Controller;

import com.example.demo.entity.CrudAuditoria;
import com.example.demo.service.CrudAuditoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST Controller para gestionar y consultar los registros de auditoría de autenticaciones.
 * Permite ver todos los registros, filtrar por fecha o email, y ordenar los resultados.
 */
@RestController
@RequestMapping("/api/auditoria")
@CrossOrigin(origins = "http://localhost:4200") // Para permitir peticiones desde Angular
public class CrudAuditoriaController {

  @Autowired
  private CrudAuditoriaService crudAuditoriaService;

  /**
   * Obtiene todos los registros de auditoría de autenticaciones.
   * Soporta ordenamiento por 'email' o 'fechaRegistroServidor'.
   *
   * @param ordenarPor El campo por el cual ordenar ("email" o "fechaRegistroServidor"). Por defecto "fechaRegistroServidor".
   * @param direccionOrdenamiento La dirección del ordenamiento ("asc" para ascendente, "desc" para descendente). Por defecto "desc".
   * @return ResponseEntity con una lista de CrudAuditoria y estado HTTP.
   *
   * Ejemplo de uso:
   * - GET /api/auditoria
   * - GET /api/auditoria?ordenarPor=email&direccionOrdenamiento=asc
   * - GET /api/auditoria?ordenarPor=fechaRegistroServidor&direccionOrdenamiento=desc
   */
  @GetMapping
  public ResponseEntity<List<CrudAuditoria>> obtenerTodasLasEntradasDeAuditoria(
    @RequestParam(defaultValue = "fechaRegistroServidor") String ordenarPor,
    @RequestParam(defaultValue = "desc") String direccionOrdenamiento) {
    try {
      List<CrudAuditoria> entries = crudAuditoriaService.findAllEntries(ordenarPor, direccionOrdenamiento);
      return new ResponseEntity<>(entries, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Obtiene todos los registros de auditoría para una fecha específica.
   * Soporta ordenamiento.
   *
   * @param fecha La fecha a buscar en formato AAAA-MM-DD.
   * @param ordenarPor El campo por el cual ordenar.
   * @param direccionOrdenamiento La dirección del ordenamiento.
   * @return ResponseEntity con una lista de CrudAuditoria para la fecha dada.
   *
   * Ejemplo de uso:
   * - GET /api/auditoria/por-fecha?fecha=2025-06-07
   * - GET /api/auditoria/por-fecha?fecha=2025-06-07&ordenarPor=email&direccionOrdenamiento=asc
   */
  @GetMapping("/por-fecha")
  public ResponseEntity<List<CrudAuditoria>> obtenerAuditoriaPorFecha(
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
    @RequestParam(defaultValue = "fechaRegistroServidor") String ordenarPor,
    @RequestParam(defaultValue = "desc") String direccionOrdenamiento) {
    try {
      List<CrudAuditoria> entries = crudAuditoriaService.findEntriesByDate(fecha, ordenarPor, direccionOrdenamiento);
      return new ResponseEntity<>(entries, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Obtiene todo el historial de accesos para un usuario buscando por email (parcial e insensible a mayúsculas/minúsculas).
   * Soporta ordenamiento.
   *
   * @param email El email o parte del email a buscar.
   * @param ordenarPor El campo por el cual ordenar.
   * @param direccionOrdenamiento La dirección del ordenamiento.
   * @return ResponseEntity con una lista de CrudAuditoria para el email dado.
   */
  @GetMapping("/por-email")
  public ResponseEntity<List<CrudAuditoria>> obtenerAuditoriaPorEmail(
    @RequestParam String email,
    @RequestParam(defaultValue = "fechaRegistroServidor") String ordenarPor,
    @RequestParam(defaultValue = "desc") String direccionOrdenamiento) {
    try {
      List<CrudAuditoria> entries = crudAuditoriaService.findEntriesByEmail(email, ordenarPor, direccionOrdenamiento);
      return new ResponseEntity<>(entries, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Obtiene el acceso de un usuario específico en una fecha específica.
   * Soporta ordenamiento.
   *
   * @param email El email exacto del usuario.
   * @param fecha La fecha a buscar en formato AAAA-MM-DD.
   * @param ordenarPor El campo por el cual ordenar.
   * @param direccionOrdenamiento La dirección del ordenamiento.
   * @return ResponseEntity con una lista de CrudAuditoria para el email y la fecha dados.

   */
  @GetMapping("/por-email-y-fecha")
  public ResponseEntity<List<CrudAuditoria>> obtenerAuditoriaPorEmailYFecha(
    @RequestParam String email,
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
    @RequestParam(defaultValue = "fechaRegistroServidor") String ordenarPor,
    @RequestParam(defaultValue = "desc") String direccionOrdenamiento) {
    try {
      List<CrudAuditoria> entries = crudAuditoriaService.findEntryByEmailAndDate(email, fecha, ordenarPor, direccionOrdenamiento);
      return new ResponseEntity<>(entries, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
