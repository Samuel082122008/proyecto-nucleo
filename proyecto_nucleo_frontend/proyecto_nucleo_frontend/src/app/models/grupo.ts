import { EstadoGrupo } from './enums';

export interface Grupo {
  id?: number;
  cursoId: number;
  estudiantesProyectados?: number;
  estado?: EstadoGrupo;
}
