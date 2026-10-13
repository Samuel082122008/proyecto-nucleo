import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CrudService } from '../core/crud.service';
import { Docente } from '../models/docente';

@Injectable({ providedIn: 'root' })
export class DocenteService extends CrudService<Docente> {
  constructor(http: HttpClient) {
    super(http, '/docentes');
  }
}
