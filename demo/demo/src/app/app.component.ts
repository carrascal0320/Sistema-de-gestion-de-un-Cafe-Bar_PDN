import { Component } from '@angular/core';
import { RouterOutlet, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet,CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'Sistema de gestion de un CafeBar';

  constructor(private router: Router) {}

  isAuthRoute(): boolean {
    return this.router.url.includes('/login') || this.router.url.includes('/register');
  }
}
