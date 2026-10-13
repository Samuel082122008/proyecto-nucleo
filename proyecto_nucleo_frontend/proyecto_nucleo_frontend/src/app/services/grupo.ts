import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from '../core/api.config';
import { Grupo } from '../models/grupo';

@Injectable({ providedIn: 'root' })
export class GrupoService {
  private url = `${API_URL}/grupos`;

  constructor(private http: HttpClient) {}

  listar(): Observable<Grupo[]> {
    return this.http.get<Grupo[]>(this.url);
  }

  crear(grupo: Grupo): Observable<Grupo> {
    return this.http.post<Grupo>(this.url, grupo);
  }

  cerrar(id: number): Observable<Grupo> {
    return this.http.patch<Grupo>(`${this.url}/${id}/cerrar`, {});
  }
}
