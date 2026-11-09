import { Component, OnInit } from '@angular/core';
import { DocenteService } from '../../services/docente';
import { UsuarioService } from '../../services/usuario';
import { AuthService } from '../../services/auth';
import { Docente } from '../../models/docente';
import { Usuario } from '../../models/usuario';
import { Rol, VINCULACIONES } from '../../models/enums';
import { textoError } from '../../core/http-error';

@Component({
  selector: 'app-docentes',
  standalone: false,
  templateUrl: './docentes.html',
  styleUrl: './docentes.css',
})
export class Docentes implements OnInit {
  docentes: Docente[] = [];
  usuarios: Usuario[] = [];
  vinculaciones = VINCULACIONES;
  form: Docente = this.vacio();
  editando = false;
  esDirector = false;
  error = '';
  mensaje = '';

  // creación de usuario
  docenteUsuario: Docente | null = null;
  nuevoUsuario = { username: '', password: '' };

  constructor(
    private service: DocenteService,
    private usuarioService: UsuarioService,
    private auth: AuthService,
  ) {}

  ngOnInit(): void {
    this.esDirector = this.auth.hasRole('DIRECTOR');
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
    this.cargarUsuarios();
  }

  private cargarUsuarios(): void {
    if (!this.esDirector) return;
    this.usuarioService.listar().subscribe({
      next: data => (this.usuarios = data),
      error: () => (this.usuarios = []),
    });
  }

  usuarioDe(d: Docente): Usuario | undefined {
    return this.usuarios.find(u => u.docenteId === d.id);
  }

  guardar(): void {
    this.limpiar();
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

  editar(d: Docente): void {
    this.form = { ...d };
    this.editando = true;
    this.limpiar();
  }

  eliminar(d: Docente): void {
    if (d.id == null || !confirm(`¿Eliminar a ${d.nombre}?`)) return;
    this.limpiar();
    this.service.eliminar(d.id).subscribe({
      next: () => this.cargar(),
      error: err => (this.error = textoError(err, 'No se pudo eliminar (puede tener datos asociados)')),
    });
  }

  cancelar(): void {
    this.form = this.vacio();
    this.editando = false;
  }

  // ---------- usuario del docente ----------

  abrirUsuario(d: Docente): void {
    this.limpiar();
    this.docenteUsuario = d;
    this.nuevoUsuario = { username: '', password: '' };
  }

  cancelarUsuario(): void {
    this.docenteUsuario = null;
  }

  crearUsuario(): void {
    this.limpiar();
    const d = this.docenteUsuario;
    if (!d || d.id == null) return;
    const { username, password } = this.nuevoUsuario;
    if (!/^[A-Za-z0-9._@-]{4,50}$/.test(username)) {
      this.error = 'Usuario: 4 a 50 caracteres (letras, números y . _ @ -)';
      return;
    }
    if (password.length < 6) {
      this.error = 'La contraseña debe tener al menos 6 caracteres';
      return;
    }
    const nuevo = { username, password, rol: 'DOCENTE' as Rol, docenteId: d.id } as Usuario;
    this.usuarioService.crear(nuevo).subscribe({
      next: () => {
        this.mensaje = `Usuario "${username}" creado para ${d.nombre}`;
        this.docenteUsuario = null;
        this.cargarUsuarios();
      },
      error: err => (this.error = textoError(err, 'No se pudo crear el usuario')),
    });
  }

  quitarUsuario(d: Docente): void {
    const u = this.usuarioDe(d);
    if (!u?.id || !confirm(`¿Quitar el usuario "${u.username}"?`)) return;
    this.limpiar();
    this.usuarioService.eliminar(u.id).subscribe({
      next: () => this.cargarUsuarios(),
      error: err => (this.error = textoError(err, 'No se pudo quitar el usuario')),
    });
  }

  private limpiar(): void {
    this.error = '';
    this.mensaje = '';
  }
}
