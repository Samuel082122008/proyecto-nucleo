import { DiaSemana } from './enums';

export interface RestriccionHoraria {
  id?: number;
  docenteId: number;
  diaSemana: DiaSemana;
  horaInicio: string; // "HH:mm"
  horaFin: string;    // "HH:mm"
}
