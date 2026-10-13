import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from '../core/api.config';
import { RestriccionHoraria } from '../models/restriccion';

@Injectable({ providedIn: 'root' })
export class RestriccionService {
  private url = `${API_URL}/restricciones`;

  constructor(private http: HttpClient) {}

  listar(docenteId?: number): Observable<RestriccionHoraria[]> {
    let params = new HttpParams();
    if (docenteId != null) params = params.set('docenteId', docenteId);
    return this.http.get<RestriccionHoraria[]>(this.url, { params });
  }

  crear(r: RestriccionHoraria): Observable<RestriccionHoraria> {
    return this.http.post<RestriccionHoraria>(this.url, r);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
