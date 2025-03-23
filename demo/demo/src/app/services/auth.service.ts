import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth'; 
  private token = signal<string | null>(localStorage.getItem('token'));

  constructor(private http: HttpClient, private router: Router) {}

  register(user: { name: string; phone: string; email: string; password: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, user);
  }

  login(credentials: { email: string; password: string }): Observable<{ token: string }> {
    return this.http.post<{ token: string }>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        localStorage.setItem('token', response.token);
        this.token.set(response.token);
      })
    );
  }

  logLogout(): void {
    localStorage.removeItem('token'); // Eliminar el token
    this.token.set(null); // Limpiar el estado
    this.router.navigate(['/login']); // Redirigir al Login
  }

  isAuthenticated(): boolean {
    return this.token() !== null;
  }
}
