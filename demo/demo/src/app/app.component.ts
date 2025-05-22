import { Component } from '@angular/core';
import { RouterOutlet, Router } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})


export class AppComponent {
  title = 'Sistema de gestion de un CafeBar';

  constructor(private router: Router) {}

  isAuthRoute(): boolean {
    return this.router.url.includes('/login') || this.router.url.includes('/register');
  }
}
