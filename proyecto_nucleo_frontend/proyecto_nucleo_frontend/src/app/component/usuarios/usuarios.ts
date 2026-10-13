import { Component, OnInit } from '@angular/core';
import { UsuarioService } from '../../services/usuario';
import { DocenteService } from '../../services/docente';
import { Usuario } from '../../models/usuario';
import { Docente } from '../../models/docente';
import { Rol } from '../../models/enums';

@Component({
  selector: 'app-usuarios',
  standalone: false,
  templateUrl: './usuarios.html',
  styleUrl: './usuarios.css',
})
export class Usuarios implements OnInit {
  usuarios: Usuario[] = [];
  docentes: Docente[] = [];
  roles: Rol[] = ['DIRECTOR', 'COORDINADOR', 'DOCENTE'];
  form: Usuario = this.vacio();
  editando = false;
  error = '';

  constructor(
    private service: UsuarioService,
    private docenteService: DocenteService,
  ) {}

  ngOnInit(): void {
    this.cargar();
    this.docenteService.listar().subscribe({
      next: data => (this.docentes = data),
    });
  }

  private vacio(): Usuario {
    return { username: '', password: '', rol: 'DOCENTE', docenteId: null };
  }

  nombreDocente(id?: number | null): string {
    return this.docentes.find(d => d.id === id)?.nombre ?? '';
  }

  cargar(): void {
    this.service.listar().subscribe({
      next: data => (this.usuarios = data),
      error: () => (this.error = 'No se pudo cargar la lista'),
    });
  }

  guardar(): void {
    this.error = '';
    if (!this.form.username.trim()) {
      this.error = 'El usuario es obligatorio';
      return;
    }
    if (!this.editando && (!this.form.password || this.form.password.length < 6)) {
      this.error = 'La contraseña debe tener al menos 6 caracteres';
      return;
    }
    if (this.editando && this.form.password && this.form.password.length < 6) {
      this.error = 'La contraseña debe tener al menos 6 caracteres';
      return;
    }

    const datos: Usuario = { ...this.form };
    if (!datos.password) delete datos.password;
    if (datos.rol !== 'DOCENTE') datos.docenteId = null;

    const peticion = this.editando && datos.id != null
      ? this.service.actualizar(datos.id, datos)
      : this.service.crear(datos);

    peticion.subscribe({
      next: () => {
        this.cancelar();
        this.cargar();
      },
      error: () => (this.error = 'No se pudo guardar (el usuario puede ya existir)'),
    });
  }

  editar(u: Usuario): void {
    this.form = { ...u, password: '' };
    this.editando = true;
    this.error = '';
  }

  eliminar(u: Usuario): void {
    if (u.id == null || !confirm(`¿Eliminar al usuario ${u.username}?`)) return;
    this.service.eliminar(u.id).subscribe({
      next: () => this.cargar(),
      error: () => (this.error = 'No se pudo eliminar'),
    });
  }

  cancelar(): void {
    this.form = this.vacio();
    this.editando = false;
  }
}
