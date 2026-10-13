import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  username = '';
  password = '';
  error = '';
  cargando = false;

  constructor(private auth: AuthService, private router: Router) {}

  ingresar(): void {
    if (!this.username || !this.password) {
      this.error = 'Ingresa usuario y contraseña';
      return;
    }
    this.error = '';
    this.cargando = true;

    this.auth.login({ username: this.username, password: this.password }).subscribe({
      next: () => {
        this.cargando = false;
        this.router.navigate(['/horario']);
      },
      error: () => {
        this.cargando = false;
        this.error = 'Usuario o contraseña incorrectos';
      },
    });
  }
}
