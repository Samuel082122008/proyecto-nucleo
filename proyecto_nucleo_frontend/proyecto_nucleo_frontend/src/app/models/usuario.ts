import { Rol } from './enums';

export interface Usuario {
  id?: number;
  username: string;
  password?: string;
  rol: Rol;
  docenteId?: number | null;
}

export interface LoginRequest {
  username: string;
  password: string;
}
