import { Component, OnInit } from '@angular/core';
import { forkJoin, of, catchError } from 'rxjs';
import { GrupoService } from '../../services/grupo';
import { CursoService } from '../../services/curso';
import { DocenteService } from '../../services/docente';
import { HabilitacionService } from '../../services/habilitacion';
import { Grupo } from '../../models/grupo';
import { Curso } from '../../models/curso';
import { Docente } from '../../models/docente';
import { Habilitacion } from '../../models/habilitacion';
import { textoError } from '../../core/http-error';

@Component({
  selector: 'app-grupos',
  standalone: false,
  templateUrl: './grupos.html',
  styleUrl: './grupos.css',
})
export class Grupos implements OnInit {
  grupos: Grupo[] = [];
  cursos: Curso[] = [];
  docentes: Docente[] = [];
  habilitaciones: Habilitacion[] = [];
  form: Grupo = this.vacio();
  error = '';

  constructor(
    private service: GrupoService,
    private cursoService: CursoService,
    private docenteService: DocenteService,
    private habilitacionService: HabilitacionService,
  ) {}

  ngOnInit(): void {
    this.cargar();
    forkJoin({
      cursos: this.cursoService.listar().pipe(catchError(() => of([] as Curso[]))),
      docentes: this.docenteService.listar().pipe(catchError(() => of([] as Docente[]))),
      habilitaciones: this.habilitacionService.listar().pipe(catchError(() => of([] as Habilitacion[]))),
    }).subscribe(r => {
      this.cursos = r.cursos;
      this.docentes = r.docentes;
      this.habilitaciones = r.habilitaciones;
    });
  }

  private vacio(): Grupo {
    return { cursoId: 0, docenteId: 0, estudiantesProyectados: 0 };
  }

  get docentesDelCurso(): Docente[] {
    const ids = this.habilitaciones.filter(h => h.cursoId === this.form.cursoId).map(h => h.docenteId);
    return this.docentes.filter(d => d.id != null && ids.includes(d.id));
  }

  elegirCurso(id: number): void {
    this.form.cursoId = id;
    this.form.docenteId = 0;
  }

  nombreCurso(id: number): string {
    return this.cursos.find(c => c.id === id)?.nombre ?? `#${id}`;
  }

  nombreDocente(id?: number): string {
    if (!id) return 'Sin docente';
    return this.docentes.find(d => d.id === id)?.nombre ?? `#${id}`;
  }

  cargar(): void {
    this.service.listar().subscribe({
      next: data => (this.grupos = data),
      error: () => (this.error = 'No se pudo cargar la lista'),
    });
  }

  guardar(): void {
    this.error = '';
    if (!this.form.cursoId) {
      this.error = 'Selecciona un curso';
      return;
    }
    if (!this.form.docenteId) {
      this.error = 'Selecciona un docente';
      return;
    }
    this.service.crear(this.form).subscribe({
      next: () => {
        this.form = this.vacio();
        this.cargar();
      },
      error: err => (this.error = textoError(err, 'No se pudo crear el grupo')),
    });
  }

  cerrar(g: Grupo): void {
    if (g.id == null || !confirm('¿Cerrar este grupo?')) return;
    this.service.cerrar(g.id).subscribe({
      next: () => this.cargar(),
      error: err => (this.error = textoError(err, 'No se pudo cerrar el grupo')),
    });
  }

  eliminar(g: Grupo): void {
    if (g.id == null || !confirm('¿Eliminar este grupo?')) return;
    this.service.eliminar(g.id).subscribe({
      next: () => this.cargar(),
      error: err => (this.error = textoError(err, 'No se pudo eliminar el grupo')),
    });
  }
}
