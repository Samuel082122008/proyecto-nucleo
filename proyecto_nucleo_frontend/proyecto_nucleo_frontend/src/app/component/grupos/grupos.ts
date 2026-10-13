import { Component, OnInit } from '@angular/core';
import { GrupoService } from '../../services/grupo';
import { CursoService } from '../../services/curso';
import { Grupo } from '../../models/grupo';
import { Curso } from '../../models/curso';

@Component({
  selector: 'app-grupos',
  standalone: false,
  templateUrl: './grupos.html',
  styleUrl: './grupos.css',
})
export class Grupos implements OnInit {
  grupos: Grupo[] = [];
  cursos: Curso[] = [];
  form: Grupo = this.vacio();
  error = '';

  constructor(
    private service: GrupoService,
    private cursoService: CursoService,
  ) {}

  ngOnInit(): void {
    this.cargar();
    this.cursoService.listar().subscribe({
      next: data => (this.cursos = data),
      error: () => (this.error = 'No se pudieron cargar los cursos'),
    });
  }

  private vacio(): Grupo {
    return { cursoId: 0, estudiantesProyectados: 0 };
  }

  nombreCurso(id: number): string {
    return this.cursos.find(c => c.id === id)?.nombre ?? `#${id}`;
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
    this.service.crear(this.form).subscribe({
      next: () => {
        this.form = this.vacio();
        this.cargar();
      },
      error: () => (this.error = 'No se pudo crear el grupo'),
    });
  }

  cerrar(g: Grupo): void {
    if (g.id == null || !confirm('¿Cerrar este grupo?')) return;
    this.service.cerrar(g.id).subscribe({
      next: () => this.cargar(),
      error: () => (this.error = 'No se pudo cerrar el grupo'),
    });
  }
}
