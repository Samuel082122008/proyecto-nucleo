import { TipoVinculacion } from './enums';

export interface Docente {
  id?: number;
  nombre: string;
  tipoVinculacion: TipoVinculacion;
  escalafon?: string;
}
