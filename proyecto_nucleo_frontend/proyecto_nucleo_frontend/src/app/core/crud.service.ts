import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from './api.config';

export abstract class CrudService<T> {
  protected url: string;

  constructor(protected http: HttpClient, path: string) {
    this.url = `${API_URL}${path}`;
  }

  listar(): Observable<T[]> {
    return this.http.get<T[]>(this.url);
  }

  buscar(id: number): Observable<T> {
    return this.http.get<T>(`${this.url}/${id}`);
  }

  crear(item: T): Observable<T> {
    return this.http.post<T>(this.url, item);
  }

  actualizar(id: number, item: T): Observable<T> {
    return this.http.put<T>(`${this.url}/${id}`, item);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
