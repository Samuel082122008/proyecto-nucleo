import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from '../core/api.config';
import { ParametroSemestre } from '../models/parametro';

@Injectable({ providedIn: 'root' })
export class ParametroService {
  private url = `${API_URL}/parametros`;

  constructor(private http: HttpClient) {}

  obtener(): Observable<ParametroSemestre> {
    return this.http.get<ParametroSemestre>(this.url);
  }

  actualizar(p: ParametroSemestre): Observable<ParametroSemestre> {
    return this.http.put<ParametroSemestre>(this.url, p);
  }
}
