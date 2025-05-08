import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';  // Importa SweetAlert2

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule
  ],
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent {
  form = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required, Validators.minLength(6)]),
    confirmPassword: new FormControl('', Validators.required)
  });

  errorMessage: string | null = null;

  constructor(private http: HttpClient, private router: Router) {}

  onSubmit() {
    this.errorMessage = null;

    // Verifica si el formulario es válido
    if (this.form.invalid) {
      this.errorMessage = 'Por favor completa todos los campos correctamente.';
      return;
    }

    const { email, password, confirmPassword } = this.form.value;

    // Verifica si las contraseñas coinciden
    if (password !== confirmPassword) {
      this.errorMessage = 'Las contraseñas no coinciden.';
      return;
    }

    // Realiza la solicitud POST al backend para restablecer la contraseña
    this.http.post('http://localhost:8080/api/auth/reset-password', { email, password })
      .subscribe({
        next: () => {
          Swal.fire({
            icon: 'success',
            title: 'Éxito',
            text: 'Contraseña restablecida con éxito',
            confirmButtonText: 'Aceptar'
          }).then(() => {
            this.router.navigate(['/login']);
          });
        },
        error: (error) => {
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: error.error?.message || 'Hubo un problema al restablecer la contraseña.',
            confirmButtonText: 'Aceptar'
          });
        }
      });
  }
}
