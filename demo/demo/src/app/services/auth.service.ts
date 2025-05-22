import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  private user = signal<any | null>(this.getStoredUser());

  constructor(private http: HttpClient, private router: Router) {
    this.checkOAuthLogin();
  }

  private getStoredUser(): any | null {
    const stored = localStorage.getItem('user');
    try {
      return stored && stored !== 'undefined' ? JSON.parse(stored) : null;
    } catch (e) {
      console.error('Error parsing stored user:', e);
      return null;
    }
  }
  

  register(user: { name: string; phone: string; email: string; password: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, user);
  }

  login(credentials: { email: string; password: string }): Observable<{ user: any }> {
    return this.http.post<{ user: any }>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        localStorage.setItem('user', JSON.stringify(response.user)); // Guardar usuario
        this.user.set(response.user);
        this.router.navigate(['/dashboard']); 
      })
    );
  }

  loginGoogle(): void {
    window.location.href = 'http://localhost:8080/oauth2/authorization/google';
  }

  loginGitHub(): void {
    window.location.href = 'http://localhost:8080/oauth2/authorization/github';
  }

  loginFacebook(): void {
    window.location.href = 'http://localhost:8080/oauth2/authorization/facebook';
  }
  

  getAuthenticatedUser(): void {
    this.http.get<any>(`${this.apiUrl}/user`).subscribe({
      next: (user) => {
        localStorage.setItem('user', JSON.stringify(user));
        this.user.set(user);
        this.router.navigate(['/dashboard']);
      },
      error: () => {
        console.error('Error obteniendo usuario autenticado');
      }
    });
  }

  checkOAuthLogin(): void {
    const urlParams = new URLSearchParams(window.location.search);
    const email = urlParams.get('email');
    const name = urlParams.get('name');

    if (email && name) {
      const userData = { email, name };
      localStorage.setItem('user', JSON.stringify(userData));
      this.user.set(userData);
      this.router.navigate(['/dashboard']);
    }
  }

  logout(): void {
    localStorage.removeItem('user');
    this.user.set(null);
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    return this.user() !== null;
  }

  getUser(): any {
    return this.user();
  }
}
