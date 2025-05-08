import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormGroup, FormBuilder , Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule
  ],
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.css'
})
export class ForgotPasswordComponent {
  form: FormGroup;
  message: string = '';
  error: string = '';
  loading: boolean = false;

  constructor(private fb: FormBuilder, private http: HttpClient) {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  onSubmit(): void {
    this.message = '';
    this.error = '';
    this.loading = true;

    const email = this.form.value.email;

    this.http.post<any>('http://localhost:8080/api/auth/request-reset', { email }).subscribe({
      next: (response) => {
        this.message = response.message;
        this.loading = false;
        this.form.reset();
      },
      error: (err) => {
        this.error = err.error.message || 'Error al enviar el correo';
        this.loading = false;
      }
    });
  }
}