import { Component, OnInit } from '@angular/core';
import { ParametroService } from '../../services/parametro';
import { ParametroSemestre } from '../../models/parametro';

@Component({
  selector: 'app-parametros',
  standalone: false,
  templateUrl: './parametros.html',
  styleUrl: './parametros.css',
})
export class Parametros implements OnInit {
  form: ParametroSemestre = {
    cupoMinimo: 1,
    cupoMaximo: 40,
    toleranciaCupo: 0,
    franjaInicio: '07:00',
    franjaFin: '21:00',
    sabadoFin: '13:00',
    almuerzoInicio: '12:00',
    almuerzoFin: '14:00',
    sesionesMaxSemana: 3,
    duracionSesionHoras: 2,
  };
  mensaje = '';
  error = '';

  constructor(private service: ParametroService) {}

  ngOnInit(): void {
    this.service.obtener().subscribe({
      next: p => {
        if (p) {
          this.form = {
            ...p,
            franjaInicio: this.hhmm(p.franjaInicio),
            franjaFin: this.hhmm(p.franjaFin),
            sabadoFin: this.hhmm(p.sabadoFin),
            almuerzoInicio: this.hhmm(p.almuerzoInicio),
            almuerzoFin: this.hhmm(p.almuerzoFin),
          };
        }
      },
      error: () => (this.error = 'No se pudieron cargar los parámetros'),
    });
  }

  private hhmm(h?: string): string {
    return (h ?? '').slice(0, 5);
  }

  guardar(): void {
    this.mensaje = '';
    this.error = '';
    const f = this.form;

    if (f.franjaInicio >= f.franjaFin) {
      this.error = 'La franja de inicio debe ser menor que la de fin';
      return;
    }
    if (f.almuerzoInicio >= f.almuerzoFin) {
      this.error = 'El inicio del almuerzo debe ser menor que el fin';
      return;
    }
    if ((f.cupoMinimo ?? 0) > (f.cupoMaximo ?? 0)) {
      this.error = 'El cupo mínimo no puede superar al máximo';
      return;
    }

    this.service.actualizar(f).subscribe({
      next: () => (this.mensaje = 'Parámetros guardados'),
      error: () => (this.error = 'No se pudieron guardar los parámetros'),
    });
  }
}
