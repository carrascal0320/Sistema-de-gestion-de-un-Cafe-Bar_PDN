import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  mostrarFormulario: string = 'login';

  constructor(private authService: AuthService, private router: Router) {}
  irAResetPassword() {
    this.router.navigate(['/reset-password']);
  }
  mostrarLogin() {
    this.mostrarFormulario = 'login';
  }

  mostrarRegistro() {
    this.mostrarFormulario = 'registro';
  }

  login() {
    if (!this.email || !this.password) {
      Swal.fire({
        icon: 'warning',
        title: 'Campos obligatorios',
        text: 'Por favor, ingresa tu correo y contraseña.',
        confirmButtonColor: '#f59e0b'
      });
      return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(this.email)) {
      Swal.fire({
        icon: 'error',
        title: 'Email inválido',
        text: 'Ingresa un email válido.',
        confirmButtonColor: '#f59e0b'
      });
      return;
    }

    this.authService.login({ email: this.email, password: this.password }).subscribe({
      next: (response) => {
        Swal.fire({
          icon: 'success',
          title: '¡Inicio de sesión exitoso!',
          text: 'Bienvenido de nuevo.',
          confirmButtonColor: '#22c55e'
        }).then(() => {
          localStorage.setItem('user', JSON.stringify(response.user));
        
        });
      },
      error: () => {
        Swal.fire({
          icon: 'error',
          title: 'Credenciales inválidas',
          text: 'Usuario o contraseña incorrectos. Inténtalo de nuevo.',
          confirmButtonColor: '#ef4444'
        });
      }
    });
  }

  //  Iniciar sesión con Google
  loginGoogle() {
    this.authService.loginGoogle();
  }

  //  Iniciar sesión con GitHub
  loginGitHub() {
    this.authService.loginGitHub();
  }

  loginFacebook(){
    this.authService.loginFacebook();
  }


}
