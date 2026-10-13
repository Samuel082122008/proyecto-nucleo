import { Component, OnInit } from '@angular/core';
import { HabilitacionService } from '../../services/habilitacion';
import { DocenteService } from '../../services/docente';
import { CursoService } from '../../services/curso';
import { Habilitacion } from '../../models/habilitacion';
import { Docente } from '../../models/docente';
import { Curso } from '../../models/curso';

@Component({
  selector: 'app-habilitaciones',
  standalone: false,
  templateUrl: './habilitaciones.html',
  styleUrl: './habilitaciones.css',
})
export class Habilitaciones implements OnInit {
  habilitaciones: Habilitacion[] = [];
  docentes: Docente[] = [];
  cursos: Curso[] = [];
  form: Habilitacion = this.vacio();
  error = '';

  constructor(
    private service: HabilitacionService,
    private docenteService: DocenteService,
    private cursoService: CursoService,
  ) {}

  ngOnInit(): void {
    this.cargar();
    this.docenteService.listar().subscribe({ next: data => (this.docentes = data) });
    this.cursoService.listar().subscribe({ next: data => (this.cursos = data) });
  }

  private vacio(): Habilitacion {
    return { docenteId: 0, cursoId: 0 };
  }

  nombreDocente(id: number): string {
    return this.docentes.find(d => d.id === id)?.nombre ?? `#${id}`;
  }

  nombreCurso(id: number): string {
    return this.cursos.find(c => c.id === id)?.nombre ?? `#${id}`;
  }

  cargar(): void {
    this.service.listar().subscribe({
      next: data => (this.habilitaciones = data),
      error: () => (this.error = 'No se pudo cargar la lista'),
    });
  }

  guardar(): void {
    this.error = '';
    if (!this.form.docenteId || !this.form.cursoId) {
      this.error = 'Selecciona docente y curso';
      return;
    }
    this.service.crear(this.form).subscribe({
      next: () => {
        this.form = this.vacio();
        this.cargar();
      },
      error: () => (this.error = 'No se pudo crear (la habilitación puede ya existir)'),
    });
  }

  eliminar(h: Habilitacion): void {
    if (h.id == null || !confirm('¿Eliminar esta habilitación?')) return;
    this.service.eliminar(h.id).subscribe({
      next: () => this.cargar(),
      error: () => (this.error = 'No se pudo eliminar'),
    });
  }
}
