import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CrudService } from '../core/crud.service';
import { Curso } from '../models/curso';

@Injectable({ providedIn: 'root' })
export class CursoService extends CrudService<Curso> {
  constructor(http: HttpClient) {
    super(http, '/cursos');
  }
}
