import { DiaSemana } from './enums';

export interface Sesion {
  id?: number;
  grupoId: number;
  docenteId: number;
  aulaId: number;
  diaSemana: DiaSemana;
  horaInicio: string; // "HH:mm"
  horaFin: string;    // "HH:mm"
}
