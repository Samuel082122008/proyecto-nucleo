export interface ParametroSemestre {
  cupoMinimo?: number;
  cupoMaximo?: number;
  toleranciaCupo?: number;
  franjaInicio: string;
  franjaFin: string;
  sabadoFin: string;
  almuerzoInicio: string;
  almuerzoFin: string;
  sesionesMaxSemana?: number;
  duracionSesionHoras?: number;
}
