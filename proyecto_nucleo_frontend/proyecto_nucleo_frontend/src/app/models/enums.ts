export type Rol = 'DIRECTOR' | 'COORDINADOR' | 'DOCENTE';

export type DiaSemana =
  | 'LUNES' | 'MARTES' | 'MIERCOLES' | 'JUEVES' | 'VIERNES' | 'SABADO';

export type TipoVinculacion =
  | 'TIEMPO_COMPLETO' | 'TRES_CUARTOS' | 'MEDIO_TIEMPO' | 'CUARTO_TIEMPO';

export type EstadoGrupo = 'ABIERTO' | 'CERRADO';

export const DIAS: DiaSemana[] =
  ['LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES', 'SABADO'];

export const VINCULACIONES: TipoVinculacion[] =
  ['TIEMPO_COMPLETO', 'TRES_CUARTOS', 'MEDIO_TIEMPO', 'CUARTO_TIEMPO'];
