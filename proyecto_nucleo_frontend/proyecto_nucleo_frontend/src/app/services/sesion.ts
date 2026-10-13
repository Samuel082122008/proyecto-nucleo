import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from '../core/api.config';
import { Sesion } from '../models/sesion';

@Injectable({ providedIn: 'root' })
export class SesionService {
  private url = `${API_URL}/sesiones`;

  constructor(private http: HttpClient) {}

  listar(): Observable<Sesion[]> {
    return this.http.get<Sesion[]>(this.url);
  }

  asignar(s: Sesion): Observable<Sesion> {
    return this.http.post<Sesion>(this.url, s);
  }

  validar(s: Sesion): Observable<any> {
    return this.http.post<any>(`${this.url}/validar`, s);
  }

  mover(id: number, s: Sesion): Observable<Sesion> {
    return this.http.put<Sesion>(`${this.url}/${id}`, s);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
