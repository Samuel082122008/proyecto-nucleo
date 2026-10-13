import { Component, OnInit } from '@angular/core';
import { AulaService } from '../../services/aula';
import { Aula } from '../../models/aula';

@Component({
  selector: 'app-aulas',
  standalone: false,
  templateUrl: './aulas.html',
  styleUrl: './aulas.css',
})
export class Aulas implements OnInit {
  aulas: Aula[] = [];
  form: Aula = this.vacio();
  editando = false;
  error = '';

  constructor(private service: AulaService) {}

  ngOnInit(): void {
    this.cargar();
  }

  private vacio(): Aula {
    return { nombre: '', capacidad: 30, tieneComputadores: false, sillasMoviles: false };
  }

  cargar(): void {
    this.service.listar().subscribe({
      next: data => (this.aulas = data),
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

  editar(a: Aula): void {
    this.form = { ...a };
    this.editando = true;
    this.error = '';
  }

  eliminar(a: Aula): void {
    if (a.id == null || !confirm(`¿Eliminar el aula ${a.nombre}?`)) return;
    this.service.eliminar(a.id).subscribe({
      next: () => this.cargar(),
      error: () => (this.error = 'No se pudo eliminar (puede tener datos asociados)'),
    });
  }

  cancelar(): void {
    this.form = this.vacio();
    this.editando = false;
  }
}
