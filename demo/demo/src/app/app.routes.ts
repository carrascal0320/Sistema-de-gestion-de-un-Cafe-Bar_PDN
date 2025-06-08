import { Routes } from '@angular/router';
import { AuthGuard } from './guards/auth.guard'; // Asegúrate de que AuthGuard esté implementado si no lo tienes
import { UserListComponent } from './user-list/user-list.component';
import { AuditoriaListComponent } from './auditoria-list/auditoria-list.component'; // Importa el nuevo componente

export const routes: Routes = [
  {
    path: 'home',
    loadComponent: () =>
      import('./home/home.component').then((m) => m.HomeComponent),
  },
  {
    path: '',
    children: [
      {
        path: '',
        redirectTo: 'home',
        pathMatch: 'full',
      },
    ],
  },
  {
    path: 'login',
    loadComponent: () =>
      import('./login/login.component').then((m) => m.LoginComponent),
  },
  {
    path: 'register',
    loadComponent: () =>
      import('./register/register.component').then((m) => m.RegisterComponent),
  },
  {
    path: 'reset-password',
    loadComponent: () =>
      import('./reset-password/reset-password.component').then(
        (m) => m.ResetPasswordComponent
      ),
  },
  {
    path: 'forgot-password',
    loadComponent: () =>
      import('./forgot-password/forgot-password.component').then(
        (m) => m.ForgotPasswordComponent
      ),
  },
  {
    path: 'dashboard',
    loadComponent: () =>
      import('./dashboard/dashboard.component').then(
        (m) => m.DashboardComponent
      ),
    canActivate: [AuthGuard], // Protegido por AuthGuard
  },
  {
    path: 'users',
    loadComponent: () =>
      import('./user-list/user-list.component').then(
        (m) => m.UserListComponent
      ),
    canActivate: [AuthGuard], // Protegido por AuthGuard
  },
  {
    path: 'auditoria', // Nueva ruta para el componente de auditoría
    loadComponent: () =>
      import('./auditoria-list/auditoria-list.component').then(
        (m) => m.AuditoriaListComponent
      ),
    canActivate: [AuthGuard], // Protegido por AuthGuard, ya que solo el administrador debe verlo
  },
  {
    path: '**', // Ruta comodín para cualquier otra URL no reconocida
    redirectTo: 'home',
  },
];
