import { ApplicationConfig } from '@angular/core';
import { provideRouter, Routes } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { AuthGuard } from './guards/auth.guard';
import { FormsModule } from '@angular/forms'; 
import { ReactiveFormsModule } from '@angular/forms';

const routes: Routes = [
  { path: '', loadComponent: () => import('./home/home.component').then(m => m.HomeComponent) },
  { path: 'login', loadComponent: () => import('./login/login.component').then(m => m.LoginComponent) },
  { path: 'register', loadComponent: () => import('./register/register.component').then(m => m.RegisterComponent) },
  { path: 'reset-password', loadComponent: () => import('./reset-password/reset-password.component').then(m => m.ResetPasswordComponent) },
  { path: 'forgot-password', loadComponent: () => import('./forgot-password/forgot-password.component').then((m) => m.ForgotPasswordComponent) },
  { path: 'dashboard', loadComponent: () => import('./dashboard/dashboard.component').then((m) => m.DashboardComponent), canActivate: [AuthGuard] },
  { path: 'users', loadComponent: () => import('./user-list/user-list.component').then(m => m.UserListComponent) }, 
  { path: 'auditoria', loadComponent: () =>  import('./auditoria-list/auditoria-list.component').then(  (m) => m.AuditoriaListComponent ),canActivate: [AuthGuard], },
  { path: '**', redirectTo: '' }
];

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(),
    FormsModule ,
    ReactiveFormsModule,
  ]
};
