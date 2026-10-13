import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CrudService } from '../core/crud.service';
import { Aula } from '../models/aula';

@Injectable({ providedIn: 'root' })
export class AulaService extends CrudService<Aula> {
  constructor(http: HttpClient) {
    super(http, '/aulas');
  }
}
