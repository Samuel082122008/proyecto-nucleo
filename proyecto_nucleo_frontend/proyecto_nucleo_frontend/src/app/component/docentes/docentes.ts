import { Component, OnInit } from '@angular/core';
import { DocenteService } from '../../services/docente';
import { Docente } from '../../models/docente';
import { VINCULACIONES } from '../../models/enums';

@Component({
  selector: 'app-docentes',
  standalone: false,
  templateUrl: './docentes.html',
  styleUrl: './docentes.css',
})
export class Docentes implements OnInit {
  docentes: Docente[] = [];
  vinculaciones = VINCULACIONES;
  form: Docente = this.vacio();
  editando = false;
  error = '';

  constructor(private service: DocenteService) {}

  ngOnInit(): void {
    this.cargar();
  }

  private vacio(): Docente {
    return { nombre: '', tipoVinculacion: 'TIEMPO_COMPLETO', escalafon: '' };
  }

  cargar(): void {
    this.service.listar().subscribe({
      next: data => (this.docentes = data),
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

  editar(d: Docente): void {
    this.form = { ...d };
    this.editando = true;
    this.error = '';
  }

  eliminar(d: Docente): void {
    if (d.id == null || !confirm(`¿Eliminar a ${d.nombre}?`)) return;
    this.service.eliminar(d.id).subscribe({
      next: () => this.cargar(),
      error: () => (this.error = 'No se pudo eliminar (puede tener datos asociados)'),
    });
  }

  cancelar(): void {
    this.form = this.vacio();
    this.editando = false;
  }
}
