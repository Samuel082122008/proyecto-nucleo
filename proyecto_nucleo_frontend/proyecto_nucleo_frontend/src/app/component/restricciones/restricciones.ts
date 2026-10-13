import { Component, OnInit } from '@angular/core';
import { RestriccionService } from '../../services/restriccion';
import { DocenteService } from '../../services/docente';
import { AuthService } from '../../services/auth';
import { RestriccionHoraria } from '../../models/restriccion';
import { Docente } from '../../models/docente';
import { DIAS } from '../../models/enums';

@Component({
  selector: 'app-restricciones',
  standalone: false,
  templateUrl: './restricciones.html',
  styleUrl: './restricciones.css',
})
export class Restricciones implements OnInit {
  restricciones: RestriccionHoraria[] = [];
  docentes: Docente[] = [];
  dias = DIAS;
  esDocente = false;
  filtroDocenteId = 0;
  form: RestriccionHoraria = this.vacio();
  error = '';

  constructor(
    private service: RestriccionService,
    private docenteService: DocenteService,
    private auth: AuthService,
  ) {}

  ngOnInit(): void {
    this.esDocente = this.auth.hasRole('DOCENTE');

    if (this.esDocente) {
      const id = this.auth.getDocenteId() ?? 0;
      this.filtroDocenteId = id;
      this.form.docenteId = id;
    } else {
      this.docenteService.listar().subscribe({ next: data => (this.docentes = data) });
    }
    this.cargar();
  }

  private vacio(): RestriccionHoraria {
    return {
      docenteId: this.filtroDocenteId,
      diaSemana: 'LUNES',
      horaInicio: '07:00',
      horaFin: '09:00',
    };
  }

  nombreDocente(id: number): string {
    return this.docentes.find(d => d.id === id)?.nombre ?? `#${id}`;
  }

  cargar(): void {
    const filtro = this.filtroDocenteId || undefined;
    this.service.listar(filtro).subscribe({
      next: data => (this.restricciones = data),
      error: () => (this.error = 'No se pudo cargar la lista'),
    });
  }

  cambiarFiltro(): void {
    this.form.docenteId = this.filtroDocenteId;
    this.cargar();
  }

  guardar(): void {
    this.error = '';
    if (!this.form.docenteId) {
      this.error = 'Selecciona un docente';
      return;
    }
    if (this.form.horaInicio >= this.form.horaFin) {
      this.error = 'La hora de inicio debe ser menor que la de fin';
      return;
    }
    this.service.crear(this.form).subscribe({
      next: () => {
        this.form = this.vacio();
        this.cargar();
      },
      error: () => (this.error = 'No se pudo crear la restricción'),
    });
  }

  eliminar(r: RestriccionHoraria): void {
    if (r.id == null || !confirm('¿Eliminar esta restricción?')) return;
    this.service.eliminar(r.id).subscribe({
      next: () => this.cargar(),
      error: () => (this.error = 'No se pudo eliminar'),
    });
  }
}
