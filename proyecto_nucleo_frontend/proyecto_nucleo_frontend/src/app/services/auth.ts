import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { API_URL } from '../core/api.config';
import { LoginRequest } from '../models/usuario';
import { Rol } from '../models/enums';

const TOKEN_KEY = 'token';

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(private http: HttpClient, private router: Router) {}

  login(data: LoginRequest): Observable<any> {
    return this.http.post<any>(`${API_URL}/auth/login`, data).pipe(
      tap(res => {
        // Se asume que la respuesta es { token: '...' }
        const token = res?.token ?? res?.accessToken ?? res?.jwt;
        if (token) localStorage.setItem(TOKEN_KEY, token);
      })
    );
  }

  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  private getPayload(): any | null {
    const token = this.getToken();
    if (!token) return null;
    try {
      const base64 = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/');
      return JSON.parse(decodeURIComponent(escape(atob(base64))));
    } catch {
      return null;
    }
  }

  isLoggedIn(): boolean {
    const payload = this.getPayload();
    if (!payload) return false;
    if (payload.exp && payload.exp * 1000 < Date.now()) {
      localStorage.removeItem(TOKEN_KEY);
      return false;
    }
    return true;
  }

  getUsername(): string {
    const p = this.getPayload();
    return p?.sub ?? p?.username ?? '';
  }

  getRol(): Rol | null {
    const p = this.getPayload();
    let rol = p?.rol ?? p?.role ?? p?.roles?.[0] ?? p?.authorities?.[0] ?? null;
    if (typeof rol === 'object' && rol?.authority) rol = rol.authority;
    return rol ? (String(rol).replace('ROLE_', '') as Rol) : null;
  }

  getDocenteId(): number | null {
    const p = this.getPayload();
    const id = p?.docenteId ?? null;
    return id != null ? Number(id) : null;
  }

  hasRole(...roles: Rol[]): boolean {
    const rol = this.getRol();
    return !!rol && roles.includes(rol);
  }
}
