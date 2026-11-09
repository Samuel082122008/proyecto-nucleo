import { Component, OnInit } from '@angular/core';
import { CursoService } from '../../services/curso';
import { Curso } from '../../models/curso';
import { textoError } from '../../core/http-error';

@Component({
  selector: 'app-cursos',
  standalone: false,
  templateUrl: './cursos.html',
  styleUrl: './cursos.css',
})
export class Cursos implements OnInit {
  cursos: Curso[] = [];
  semestres = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
  filtroSemestre = 0; // 0 = todos, -1 = sin semestre
  form: Curso = this.vacio();
  editando = false;
  error = '';

  constructor(private service: CursoService) {}

  ngOnInit(): void {
    this.cargar();
  }

  private vacio(): Curso {
    return {
      nombre: '',
      sesionesPorSemana: 1,
      requiereComputadores: false,
      requiereSillasMoviles: false,
      semestre: null,
    };
  }

  get cursosVisibles(): Curso[] {
    if (this.filtroSemestre === 0) return this.cursos;
    if (this.filtroSemestre === -1) return this.cursos.filter(c => c.semestre == null);
    return this.cursos.filter(c => c.semestre === this.filtroSemestre);
  }

  cargar(): void {
    this.service.listar().subscribe({
      next: data => (this.cursos = data),
      error: () => (this.error = 'No se pudo cargar la lista'),
    });
  }

  guardar(): void {
    this.error = '';
    if (!this.form.nombre.trim()) {
      this.error = 'El nombre es obligatorio';
      return;
    }
    const peticion = this.editando && this.form.id != null
      ? this.service.actualizar(this.form.id, this.form)
      : this.service.crear(this.form);

    peticion.subscribe({
      next: () => {
        this.cancelar();
        this.cargar();
      },
      error: err => (this.error = textoError(err, 'No se pudo guardar')),
    });
  }

  editar(c: Curso): void {
    this.form = { ...c };
    this.editando = true;
    this.error = '';
  }

  eliminar(c: Curso): void {
    if (c.id == null || !confirm(`¿Eliminar el curso ${c.nombre}?`)) return;
    this.error = '';
    this.service.eliminar(c.id).subscribe({
      next: () => this.cargar(),
      error: err => (this.error = textoError(err, 'No se pudo eliminar (puede tener datos asociados)')),
    });
  }

  cancelar(): void {
    this.form = this.vacio();
    this.editando = false;
  }
}
