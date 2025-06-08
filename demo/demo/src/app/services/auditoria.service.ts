    import { Injectable } from '@angular/core';
    import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
    import { Observable, throwError } from 'rxjs';
    import { catchError } from 'rxjs/operators';
    import { CrudAuditoria } from '../models/crud-auditoria.model';

    @Injectable({
      providedIn: 'root'
    })
    export class AuditoriaService {
      private apiUrl = 'http://localhost:8080/api/auditoria';

      constructor(private http: HttpClient) { }

      private getOptions() {
        return { withCredentials: true }; // Esta línea es CRUCIAL para enviar cookies de sesión
      }

      getTodasEntradasAuditoria(ordenarPor: string = 'fechaRegistroServidor', direccionOrdenamiento: string = 'desc'): Observable<CrudAuditoria[]> {
        let params = new HttpParams();
        params = params.append('ordenarPor', ordenarPor);
        params = params.append('direccionOrdenamiento', direccionOrdenamiento);

        return this.http.get<CrudAuditoria[]>(this.apiUrl, { params, ...this.getOptions() }).pipe( // Aplicar opciones aquí
          catchError(this.handleError)
        );
      }

      getAuditoriaPorFecha(fecha: string, ordenarPor: string = 'fechaRegistroServidor', direccionOrdenamiento: string = 'desc'): Observable<CrudAuditoria[]> {
        let params = new HttpParams();
        params = params.append('fecha', fecha);
        params = params.append('ordenarPor', ordenarPor);
        params = params.append('direccionOrdenamiento', direccionOrdenamiento);

        return this.http.get<CrudAuditoria[]>(`${this.apiUrl}/por-fecha`, { params, ...this.getOptions() }).pipe( // Aplicar opciones
          catchError(this.handleError)
        );
      }

      getAuditoriaPorEmail(email: string, ordenarPor: string = 'fechaRegistroServidor', direccionOrdenamiento: string = 'desc'): Observable<CrudAuditoria[]> {
        let params = new HttpParams();
        params = params.append('email', email);
        params = params.append('ordenarPor', ordenarPor);
        params = params.append('direccionOrdenamiento', direccionOrdenamiento);

        return this.http.get<CrudAuditoria[]>(`${this.apiUrl}/por-email`, { params, ...this.getOptions() }).pipe( // Aplicar opciones
          catchError(this.handleError)
        );
      }

      getAuditoriaPorEmailYFecha(email: string, fecha: string, ordenarPor: string = 'fechaRegistroServidor', direccionOrdenamiento: string = 'desc'): Observable<CrudAuditoria[]> {
        let params = new HttpParams();
        params = params.append('email', email);
        params = params.append('fecha', fecha);
        params = params.append('ordenarPor', ordenarPor);
        params = params.append('direccionOrdenamiento', direccionOrdenamiento);

        return this.http.get<CrudAuditoria[]>(`${this.apiUrl}/por-email-y-fecha`, { params, ...this.getOptions() }).pipe( // Aplicar opciones
          catchError(this.handleError)
        );
      }

      private handleError(error: HttpErrorResponse) {
        let errorMessage = 'Ocurrió un error desconocido';
        if (error.error instanceof ErrorEvent) {
          errorMessage = `Error: ${error.error.message}`;
        } else if (error.status === 0) {
          errorMessage = 'No se puede conectar al servidor. Verifica que el backend esté ejecutándose en el puerto 8080 y la URL de la API sea correcta.';
        } else {
          errorMessage = `Error ${error.status}: ${error.message || JSON.stringify(error.error)}`;
        }
        console.error('Error completo en AuditoriaService:', error);
        return throwError(() => new Error(errorMessage));
      }
    }
    