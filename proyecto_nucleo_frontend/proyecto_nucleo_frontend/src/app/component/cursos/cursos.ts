import { Component, OnInit } from '@angular/core';
import { CursoService } from '../../services/curso';
import { Curso } from '../../models/curso';

@Component({
  selector: 'app-cursos',
  standalone: false,
  templateUrl: './cursos.html',
  styleUrl: './cursos.css',
})
export class Cursos implements OnInit {
  cursos: Curso[] = [];
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
    };
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
      error: () => (this.error = 'No se pudo guardar'),
    });
  }

  editar(c: Curso): void {
    this.form = { ...c };
    this.editando = true;
    this.error = '';
  }

  eliminar(c: Curso): void {
    if (c.id == null || !confirm(`¿Eliminar el curso ${c.nombre}?`)) return;
    this.service.eliminar(c.id).subscribe({
      next: () => this.cargar(),
      error: () => (this.error = 'No se pudo eliminar (puede tener datos asociados)'),
    });
  }

  cancelar(): void {
    this.form = this.vacio();
    this.editando = false;
  }
}
