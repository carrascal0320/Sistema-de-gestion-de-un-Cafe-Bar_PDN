import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CrudAuditoria } from '../models/crud-auditoria.model';
import { AuditoriaService } from '../services/auditoria.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-auditoria-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './auditoria-list.component.html',
  styleUrls: ['./auditoria-list.component.css'] 
})
export class AuditoriaListComponent implements OnInit {
  auditoriaEntries: CrudAuditoria[] = [];
  loading = true;
  error: string | null = null;

  // Parámetros de ordenamiento
  ordenarPor: string = 'fechaRegistroServidor'; // 'fechaRegistroServidor' o 'email'
  direccionOrdenamiento: string = 'desc'; // 'asc' o 'desc'

  // Parámetros de filtro/búsqueda
  filtroFecha: string = ''; // Para la fecha específica (AAAA-MM-DD)
  busquedaEmail: string = ''; // Para la búsqueda por email (parcial)
  filtroEmailYFecha: { email: string, fecha: string } = { email: '', fecha: '' }; // Para búsqueda combinada

  constructor(private auditoriaService: AuditoriaService) { }

  ngOnInit(): void {
    this.cargarAuditoria();
  }

  /**
   * Carga los registros de auditoría aplicando los filtros y ordenamientos actuales.
   * Utiliza una lógica condicional para llamar al endpoint de backend correcto.
   */
  cargarAuditoria(): void {
    this.loading = true;
    this.error = null; // Limpiar errores anteriores

    let observable: Observable<CrudAuditoria[]>;

    if (this.filtroEmailYFecha.email && this.filtroEmailYFecha.fecha) {
      // Caso 4: Buscar por email y fecha específicos
      observable = this.auditoriaService.getAuditoriaPorEmailYFecha(
        this.filtroEmailYFecha.email,
        this.filtroEmailYFecha.fecha,
        this.ordenarPor,
        this.direccionOrdenamiento
      );
    } else if (this.busquedaEmail) {
      // Caso 3: Buscar por email (historial)
      observable = this.auditoriaService.getAuditoriaPorEmail(
        this.busquedaEmail,
        this.ordenarPor,
        this.direccionOrdenamiento
      );
    } else if (this.filtroFecha) {
      // Caso 2: Buscar por fecha específica
      observable = this.auditoriaService.getAuditoriaPorFecha(
        this.filtroFecha,
        this.ordenarPor,
        this.direccionOrdenamiento
      );
    } else {
      // Caso 1: Obtener todos los registros
      observable = this.auditoriaService.getTodasEntradasAuditoria(
        this.ordenarPor,
        this.direccionOrdenamiento
      );
    }

    observable.subscribe({
      next: (data) => {
        this.auditoriaEntries = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = err.message || 'Error desconocido al cargar los registros de auditoría.';
        this.loading = false;
      }
    });
  }

  /**
   * Aplica la búsqueda/filtrado al hacer clic en el botón o cambiar los parámetros.
   * Restablece los filtros conflictivos para evitar llamadas erróneas al backend.
   */
  aplicarFiltros(): void {
    // Lógica para asegurar que solo un tipo de filtro principal esté activo si es excluyente
    if (this.filtroEmailYFecha.email || this.filtroEmailYFecha.fecha) {
      this.busquedaEmail = ''; // Desactiva la búsqueda por email simple
      this.filtroFecha = '';    // Desactiva el filtro por fecha simple
    } else if (this.busquedaEmail) {
      this.filtroFecha = '';
      this.filtroEmailYFecha = { email: '', fecha: '' };
    } else if (this.filtroFecha) {
      this.busquedaEmail = '';
      this.filtroEmailYFecha = { email: '', fecha: '' };
    }
    this.cargarAuditoria();
  }

  /**
   * Limpia todos los filtros y vuelve a cargar todos los registros.
   */
  limpiarFiltros(): void {
    this.filtroFecha = '';
    this.busquedaEmail = '';
    this.filtroEmailYFecha = { email: '', fecha: '' };
    this.ordenarPor = 'fechaRegistroServidor';
    this.direccionOrdenamiento = 'desc';
    this.cargarAuditoria();
  }

  /**
   * Formatea la fecha y hora para mostrarla de forma legible en la tabla.
   * @param dateTimeString La cadena de fecha y hora ISO del backend.
   * @returns La fecha y hora formateada.
   */
  formatoFechaHora(dateTimeString: string): string {
    if (!dateTimeString) return 'N/A';
    try {
      const date = new Date(dateTimeString);
      // Opciones de formato, puedes ajustarlas según tus preferencias
      const options: Intl.DateTimeFormatOptions = {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: false // Formato 24 horas
      };
      return date.toLocaleString('es-ES', options);
    } catch (e) {
      console.error("Error formateando fecha:", e);
      return dateTimeString; // Retorna la cadena original si falla el formato
    }
  }
}
