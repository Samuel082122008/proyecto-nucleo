import { Component, OnInit } from '@angular/core';
import { forkJoin, of, catchError } from 'rxjs';
import { SesionService } from '../../services/sesion';
import { GrupoService } from '../../services/grupo';
import { CursoService } from '../../services/curso';
import { DocenteService } from '../../services/docente';
import { AulaService } from '../../services/aula';
import { ParametroService } from '../../services/parametro';
import { AuthService } from '../../services/auth';
import { Sesion } from '../../models/sesion';
import { Grupo } from '../../models/grupo';
import { Curso } from '../../models/curso';
import { Docente } from '../../models/docente';
import { Aula } from '../../models/aula';
import { DIAS } from '../../models/enums';

@Component({
  selector: 'app-horario',
  standalone: false,
  templateUrl: './horario.html',
  styleUrl: './horario.css',
})
export class Horario implements OnInit {
  sesiones: Sesion[] = [];
  grupos: Grupo[] = [];
  cursos: Curso[] = [];
  docentes: Docente[] = [];
  aulas: Aula[] = [];

  dias = DIAS;
  horas: string[] = [];
  almuerzoInicio = '';
  almuerzoFin = '';

  puedeEditar = false;
  form: Sesion = this.vacio();
  editandoId: number | null = null;
  mensaje = '';
  error = '';

  constructor(
    private service: SesionService,
    private grupoService: GrupoService,
    private cursoService: CursoService,
    private docenteService: DocenteService,
    private aulaService: AulaService,
    private parametroService: ParametroService,
    private auth: AuthService,
  ) {}

  ngOnInit(): void {
    this.puedeEditar = this.auth.hasRole('DIRECTOR', 'COORDINADOR');
    this.generarHoras('07:00', '21:00');

    forkJoin({
      grupos: this.grupoService.listar().pipe(catchError(() => of([] as Grupo[]))),
      cursos: this.cursoService.listar().pipe(catchError(() => of([] as Curso[]))),
      docentes: this.docenteService.listar().pipe(catchError(() => of([] as Docente[]))),
      aulas: this.aulaService.listar().pipe(catchError(() => of([] as Aula[]))),
      parametros: this.parametroService.obtener().pipe(catchError(() => of(null))),
    }).subscribe(r => {
      this.grupos = r.grupos;
      this.cursos = r.cursos;
      this.docentes = r.docentes;
      this.aulas = r.aulas;
      if (r.parametros) {
        this.generarHoras(r.parametros.franjaInicio, r.parametros.franjaFin);
        this.almuerzoInicio = this.hhmm(r.parametros.almuerzoInicio);
        this.almuerzoFin = this.hhmm(r.parametros.almuerzoFin);
      }
    });

    this.cargar();
  }

  private vacio(): Sesion {
    return { grupoId: 0, docenteId: 0, aulaId: 0, diaSemana: 'LUNES', horaInicio: '07:00', horaFin: '09:00' };
  }

  hhmm(h?: string): string {
    return (h ?? '').slice(0, 5);
  }

  private generarHoras(inicio: string, fin: string): void {
    const a = parseInt(inicio, 10);
    const b = parseInt(fin, 10);
    if (isNaN(a) || isNaN(b) || b <= a) return;
    this.horas = [];
    for (let h = a; h < b; h++) {
      this.horas.push(`${String(h).padStart(2, '0')}:00`);
    }
  }

  esAlmuerzo(hora: string): boolean {
    return !!this.almuerzoInicio && hora >= this.almuerzoInicio && hora < this.almuerzoFin;
  }

  nombreGrupo(id: number): string {
    const g = this.grupos.find(x => x.id === id);
    if (!g) return `Grupo #${id}`;
    const curso = this.cursos.find(c => c.id === g.cursoId)?.nombre ?? `Curso #${g.cursoId}`;
    return `${curso} (G${g.id})`;
  }

  nombreDocente(id: number): string {
    return this.docentes.find(d => d.id === id)?.nombre ?? `Docente #${id}`;
  }

  nombreAula(id: number): string {
    return this.aulas.find(a => a.id === id)?.nombre ?? `Aula #${id}`;
  }

  sesionesEn(dia: string, hora: string): Sesion[] {
    return this.sesiones.filter(
      s => s.diaSemana === dia && this.hhmm(s.horaInicio) <= hora && hora < this.hhmm(s.horaFin),
    );
  }

  cargar(): void {
    this.service.listar().subscribe({
      next: data => {
        const miId = this.auth.getDocenteId();
        this.sesiones = !this.puedeEditar && miId != null
          ? data.filter(s => s.docenteId === miId)
          : data;
      },
      error: () => (this.error = 'No se pudo cargar el horario'),
    });
  }

  private datosValidos(): boolean {
    this.mensaje = '';
    this.error = '';
    const f = this.form;
    if (!f.grupoId || !f.docenteId || !f.aulaId) {
      this.error = 'Selecciona grupo, docente y aula';
      return false;
    }
    if (f.horaInicio >= f.horaFin) {
      this.error = 'La hora de inicio debe ser menor que la de fin';
      return false;
    }
    return true;
  }

  private textoError(err: any, defecto: string): string {
    const e = err?.error;
    if (typeof e === 'string' && e) return e;
    return e?.message ?? e?.mensaje ?? e?.error ?? defecto;
  }

  validar(): void {
    if (!this.datosValidos()) return;
    this.service.validar(this.form).subscribe({
      next: res => {
        const texto = typeof res === 'string' ? res : res?.mensaje ?? res?.message;
        this.mensaje = texto || 'La sesión es válida';
      },
      error: err => (this.error = this.textoError(err, 'La sesión no es válida')),
    });
  }

  guardar(): void {
    if (!this.datosValidos()) return;

    const peticion = this.editandoId != null
      ? this.service.mover(this.editandoId, this.form)
      : this.service.asignar(this.form);

    peticion.subscribe({
      next: () => {
        this.cancelar();
        this.mensaje = 'Guardado correctamente';
        this.cargar();
      },
      error: err => (this.error = this.textoError(err, 'No se pudo guardar la sesión')),
    });
  }

  editar(s: Sesion): void {
    if (!this.puedeEditar) return;
    this.form = { ...s, horaInicio: this.hhmm(s.horaInicio), horaFin: this.hhmm(s.horaFin) };
    this.editandoId = s.id ?? null;
    this.mensaje = '';
    this.error = '';
  }

  eliminar(s: Sesion): void {
    if (s.id == null || !confirm('¿Eliminar esta sesión?')) return;
    this.service.eliminar(s.id).subscribe({
      next: () => {
        this.cancelar();
        this.cargar();
      },
      error: err => (this.error = this.textoError(err, 'No se pudo eliminar')),
    });
  }

  cancelar(): void {
    this.form = this.vacio();
    this.editandoId = null;
  }
}
