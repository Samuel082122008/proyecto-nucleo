import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from '../core/api.config';
import { Habilitacion } from '../models/habilitacion';

@Injectable({ providedIn: 'root' })
export class HabilitacionService {
  private url = `${API_URL}/habilitaciones`;

  constructor(private http: HttpClient) {}

  listar(): Observable<Habilitacion[]> {
    return this.http.get<Habilitacion[]>(this.url);
  }

  crear(h: Habilitacion): Observable<Habilitacion> {
    return this.http.post<Habilitacion>(this.url, h);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
