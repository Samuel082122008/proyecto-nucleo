import { EstadoGrupo } from './enums';

export interface Grupo {
  id?: number;
  cursoId: number;
  docenteId?: number;
  estudiantesProyectados?: number;
  estado?: EstadoGrupo;
}
