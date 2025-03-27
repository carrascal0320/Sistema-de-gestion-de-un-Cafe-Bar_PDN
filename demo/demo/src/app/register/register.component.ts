import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  name: string = '';
  phone: string = '';
  email: string = '';
  password: string = '';

  constructor(private authService: AuthService) {}

  register() {
    // Validaciones
    if (!this.name || !this.phone || !this.email || !this.password) {
      Swal.fire({
        icon: 'warning',
        title: 'Campos obligatorios',
        text: 'Por favor, completa todos los campos.',
        confirmButtonColor: '#f59e0b'
      });
      return;
    }

    // Validación de email
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

    
    const phoneRegex = /^[0-9]{10,15}$/;
    if (!phoneRegex.test(this.phone)) {
      Swal.fire({
        icon: 'error',
        title: 'Teléfono inválido',
        text: 'El teléfono debe contener entre 10 y 15 dígitos numéricos.',
        confirmButtonColor: '#f59e0b'
      });
      return;
    }

    const userData = {
      name: this.name,
      phone: this.phone,
      email: this.email,
      password: this.password
    };

    this.authService.register(userData).subscribe(
      response => {
        Swal.fire({
          icon: 'success',
          title: '¡Registro exitoso!',
          text: 'Tu cuenta ha sido creada correctamente.',
          confirmButtonColor: '#22c55e'
        }).then(() => {
        
          window.location.href = '/login';
        });
      },
      error => {
        Swal.fire({
          icon: 'error',
          title: 'Error en el registro',
          text: 'Hubo un problema al registrarte. Inténtalo de nuevo.',
          confirmButtonColor: '#ef4444'
        });
      }
    );
  }
}
