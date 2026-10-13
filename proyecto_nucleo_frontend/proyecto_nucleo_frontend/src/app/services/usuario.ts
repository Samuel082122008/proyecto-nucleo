import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CrudService } from '../core/crud.service';
import { Usuario } from '../models/usuario';

@Injectable({ providedIn: 'root' })
export class UsuarioService extends CrudService<Usuario> {
  constructor(http: HttpClient) {
    super(http, '/usuarios');
  }
}
