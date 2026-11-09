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
import { textoError } from '../../core/http-error';

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
  pendientes: { grupo: Grupo; faltan: number }[] = [];

  dias = DIAS;
  horas: string[] = [];
  almuerzoInicio = '';
  almuerzoFin = '';
  duracionHoras = 2;

  puedeEditar = false;
  form: Sesion = this.vacio();
  editandoId: number | null = null;
  aulaElegida = 0; // 0 = automática
  mensaje = '';
  error = '';

  arrastrando: Sesion | null = null;
  arrastrandoGrupo: Grupo | null = null;
  celdaHover = '';

  semestres = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
  filtroSemestre = 0; // 0 = todos
  filtroCurso = 0;
  filtroDocente = 0;
  filtroAula = 0;
  visibles = 0;

  celdas = new Map<string, Sesion[]>();
  private readonly VACIO: Sesion[] = [];

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
        const d = (r.parametros as any).duracionSesionHoras;
        if (d) this.duracionHoras = Number(d);
      }
      this.calcularPendientes();
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
    this.reconstruir();
  }

  esAlmuerzo(hora: string): boolean {
    return !!this.almuerzoInicio && hora >= this.almuerzoInicio && hora < this.almuerzoFin;
  }

  nombreGrupo(id: number): string {
    const g = this.grupos.find(x => x.id === id);
    if (!g) return `Grupo #${id}`;
    const curso = this.cursos.find(c => c.id === g.cursoId);
    const sem = curso?.semestre ? `S${curso.semestre} · ` : '';
    return `${sem}${curso?.nombre ?? `Curso #${g.cursoId}`} (G${g.id})`;
  }

  nombreDocente(id: number): string {
    return this.docentes.find(d => d.id === id)?.nombre ?? `Docente #${id}`;
  }

  docenteDelGrupo(grupoId?: number): string {
    const g = this.grupos.find(x => x.id === grupoId);
    if (!g?.docenteId) return '—';
    return this.nombreDocente(g.docenteId);
  }

  nombreAula(id: number): string {
    return this.aulas.find(a => a.id === id)?.nombre ?? `Aula #${id}`;
  }

  sesionesEn(dia: string, hora: string): Sesion[] {
    return this.celdas.get(`${dia}|${hora}`) ?? this.VACIO;
  }

  private reconstruir(): void {
    const mapa = new Map<string, Sesion[]>();
    const mostradas = this.sesiones.filter(s => this.pasaFiltro(s));
    this.visibles = mostradas.length;
    for (const s of mostradas) {
      const ini = this.hhmm(s.horaInicio);
      const fin = this.hhmm(s.horaFin);
      for (const h of this.horas) {
        if (ini <= h && h < fin) {
          const k = `${s.diaSemana}|${h}`;
          const lista = mapa.get(k);
          if (lista) lista.push(s); else mapa.set(k, [s]);
        }
      }
    }
    this.celdas = mapa;
    this.calcularPendientes();
  }
  private aplicar(res: any): void {
    const nueva: Sesion | undefined = res?.sesion;
    if (!nueva) {
      this.cargar();
      return;
    }
    const i = this.sesiones.findIndex(x => x.id === nueva.id);
    this.sesiones = i >= 0
      ? this.sesiones.map(x => (x.id === nueva.id ? nueva : x))
      : [...this.sesiones, nueva];
    this.reconstruir();
  }

  cargar(): void {
    this.service.listar().subscribe({
      next: data => {
        const miId = this.auth.getDocenteId();
        this.sesiones = !this.puedeEditar && miId != null
          ? data.filter(s => s.docenteId === miId)
          : data;
        this.reconstruir();
      },
      error: () => (this.error = 'No se pudo cargar el horario'),
    });
  }

  private calcularPendientes(): void {
    this.pendientes = this.grupos
      .filter(g => g.estado !== 'CERRADO')
      .filter(g => this.pasaFiltroGrupo(g))
      .map(g => {
        const total = this.cursos.find(c => c.id === g.cursoId)?.sesionesPorSemana ?? 1;
        const hechas = this.sesiones.filter(s => s.grupoId === g.id).length;
        return { grupo: g, faltan: total - hechas };
      })
      .filter(p => p.faltan > 0);
  }

  // ---------- helpers ----------

  private limpiar(): void {
    this.mensaje = '';
    this.error = '';
  }

  private exito(res: any, base: string): string {
    const av: string[] = res?.advertencias ?? [];
    return av.length ? `${base}. Aviso: ${av.join(' · ')}` : base;
  }

  private conDocente(s: Sesion): Sesion {
    const g = this.grupos.find(x => x.id === s.grupoId);
    return { ...s, docenteId: g?.docenteId ?? s.docenteId ?? 0 };
  }

  private aMin(h: string): number {
    const [hh, mm] = this.hhmm(h).split(':').map(Number);
    return hh * 60 + (mm || 0);
  }

  private aHora(min: number): string {
    const h = Math.floor(min / 60);
    const m = min % 60;
    return `${String(h).padStart(2, '0')}:${String(m).padStart(2, '0')}`;
  }

  // ---------- formulario ----------

  private datosValidos(): boolean {
    this.limpiar();
    const f = this.form;
    if (!f.grupoId || !f.aulaId) {
      this.error = 'Selecciona grupo y aula';
      return false;
    }
    if (f.horaInicio >= f.horaFin) {
      this.error = 'La hora de inicio debe ser menor que la de fin';
      return false;
    }
    return true;
  }

  validar(): void {
    if (!this.datosValidos()) return;
    this.service.validar(this.conDocente(this.form)).subscribe({
      next: res => {
        if (res.valido) {
          this.mensaje = res.advertencias?.length
            ? 'Válida, con avisos: ' + res.advertencias.join(' · ')
            : 'La sesión es válida';
        } else {
          this.error = res.errores.join(' · ');
        }
      },
      error: err => (this.error = textoError(err, 'No se pudo validar la sesión')),
    });
  }

  guardar(): void {
    if (!this.datosValidos()) return;
    const payload = this.conDocente(this.form);
    const peticion = this.editandoId != null
      ? this.service.mover(this.editandoId, payload)
      : this.service.asignar(payload);

    peticion.subscribe({
      next: (res: any) => {
        this.cancelar();
        this.mensaje = this.exito(res, 'Guardado correctamente');
        this.aplicar(res);
      },
      error: err => (this.error = textoError(err, 'No se pudo guardar la sesión')),
    });
  }

  editar(s: Sesion): void {
    if (!this.puedeEditar) return;
    this.form = { ...s, horaInicio: this.hhmm(s.horaInicio), horaFin: this.hhmm(s.horaFin) };
    this.editandoId = s.id ?? null;
    this.limpiar();
  }

  eliminar(s: Sesion): void {
    if (s.id == null || !confirm('¿Eliminar esta sesión?')) return;
    this.service.eliminar(s.id).subscribe({
      next: () => {
        this.cancelar();
        this.sesiones = this.sesiones.filter(x => x.id !== s.id);
        this.reconstruir();
      },
      error: err => (this.error = textoError(err, 'No se pudo eliminar')),
    });
  }

  cancelar(): void {
    this.form = this.vacio();
    this.editandoId = null;
  }

  // ---------- drag and drop ----------

  iniciarArrastre(ev: DragEvent, s: Sesion): void {
    if (!this.puedeEditar) return;
    this.arrastrando = s;
    this.arrastrandoGrupo = null;
    ev.dataTransfer!.effectAllowed = 'move';
    ev.dataTransfer!.setData('text/plain', String(s.id));
  }

  iniciarArrastreGrupo(ev: DragEvent, g: Grupo): void {
    if (!this.puedeEditar) return;
    this.arrastrandoGrupo = g;
    this.arrastrando = null;
    ev.dataTransfer!.effectAllowed = 'copy';
    ev.dataTransfer!.setData('text/plain', `grupo-${g.id}`);
  }

  finArrastre(): void {
    this.arrastrando = null;
    this.arrastrandoGrupo = null;
    this.celdaHover = '';
  }

  sobreCelda(ev: DragEvent, dia: string, hora: string): void {
    if (!this.arrastrando && !this.arrastrandoGrupo) return;
    ev.preventDefault(); // necesario para permitir el drop
    ev.dataTransfer!.dropEffect = this.arrastrandoGrupo ? 'copy' : 'move';
    const clave = dia + hora;
    if (this.celdaHover !== clave) this.celdaHover = clave;
  }

  soltar(ev: DragEvent, dia: string, hora: string): void {
    ev.preventDefault();
    const sesion = this.arrastrando;
    const grupo = this.arrastrandoGrupo;
    this.finArrastre();

    if (grupo) {
      this.crearDesdeGrupo(grupo, dia, hora);
    } else if (sesion) {
      this.moverSesion(sesion, dia, hora);
    }
  }

  private moverSesion(s: Sesion, dia: string, hora: string): void {
    if (s.id == null) return;
    if (s.diaSemana === dia && this.hhmm(s.horaInicio) === hora) return;

    const duracion = this.aMin(s.horaFin) - this.aMin(s.horaInicio);
    const fin = this.aMin(hora) + duracion;
    this.limpiar();
    if (fin > 24 * 60) {
      this.error = 'La sesión no cabe en esa franja';
      return;
    }

    const nueva: Sesion = {
      ...s,
      diaSemana: dia as Sesion['diaSemana'],
      horaInicio: hora,
      horaFin: this.aHora(fin),
    };

    this.service.mover(s.id, this.conDocente(nueva)).subscribe({
      next: (res: any) => {
        this.mensaje = this.exito(res, 'Sesión movida correctamente');
        this.aplicar(res);
      },
      error: err => (this.error = textoError(err, 'No se pudo mover la sesión')),
    });
  }

  private crearDesdeGrupo(g: Grupo, dia: string, hora: string): void {
    this.limpiar();
    const fin = this.aMin(hora) + this.duracionHoras * 60;
    if (fin > 24 * 60) {
      this.error = 'La sesión no cabe en esa franja';
      return;
    }
    const horaFin = this.aHora(fin);
    const aulaId = this.aulaElegida || this.sugerirAula(g, dia, hora, horaFin);
    if (!aulaId) {
      this.error = 'No hay un aula libre y compatible en ese horario. Elige un aula manualmente.';
      return;
    }

    const nueva: Sesion = {
      grupoId: g.id!,
      docenteId: g.docenteId ?? 0,
      aulaId,
      diaSemana: dia as Sesion['diaSemana'],
      horaInicio: hora,
      horaFin,
    };

    this.service.asignar(nueva).subscribe({
      next: (res: any) => {
        this.mensaje = this.exito(res, 'Sesión creada correctamente');
        this.aplicar(res);
      },
      error: err => (this.error = textoError(err, 'No se pudo crear la sesión')),
    });
  }

  /** Aula más pequeña que quepa, tenga lo que pide el curso y esté libre en ese bloque. */
  private sugerirAula(g: Grupo, dia: string, ini: string, fin: string): number {
    const curso = this.cursos.find(c => c.id === g.cursoId);
    const a0 = this.aMin(ini);
    const a1 = this.aMin(fin);
    const candidatas = this.aulas
      .filter(a => (a.capacidad ?? 0) >= (g.estudiantesProyectados ?? 0))
      .filter(a => !curso?.requiereComputadores || a.tieneComputadores)
      .filter(a => !curso?.requiereSillasMoviles || a.sillasMoviles)
      .filter(a => !this.sesiones.some(s =>
        s.aulaId === a.id && s.diaSemana === dia
        && this.aMin(s.horaInicio) < a1 && this.aMin(s.horaFin) > a0))
      .sort((x, y) => (x.capacidad ?? 0) - (y.capacidad ?? 0));
    return candidatas[0]?.id ?? 0;
  }
  get cursosDelFiltro(): Curso[] {
    return this.cursos.filter(c => !this.filtroSemestre || c.semestre === this.filtroSemestre);
  }

  get hayFiltros(): boolean {
    return !!(this.filtroSemestre || this.filtroCurso || this.filtroDocente || this.filtroAula);
  }

  elegirSemestre(n: number): void {
    this.filtroSemestre = this.filtroSemestre === n ? 0 : n;
    if (this.filtroCurso && !this.cursosDelFiltro.some(c => c.id === this.filtroCurso)) {
      this.filtroCurso = 0;
    }
    this.reconstruir();
  }

  aplicarFiltros(): void {
    this.reconstruir();
  }

  limpiarFiltros(): void {
    this.filtroSemestre = this.filtroCurso = this.filtroDocente = this.filtroAula = 0;
    this.reconstruir();
  }

  private pasaFiltro(s: Sesion): boolean {
    const g = this.grupos.find(x => x.id === s.grupoId);
    const c = this.cursos.find(x => x.id === g?.cursoId);
    if (this.filtroSemestre && c?.semestre !== this.filtroSemestre) return false;
    if (this.filtroCurso && g?.cursoId !== this.filtroCurso) return false;
    if (this.filtroDocente && s.docenteId !== this.filtroDocente) return false;
    if (this.filtroAula && s.aulaId !== this.filtroAula) return false;
    return true;
  }

  private pasaFiltroGrupo(g: Grupo): boolean {
    const c = this.cursos.find(x => x.id === g.cursoId);
    if (this.filtroSemestre && c?.semestre !== this.filtroSemestre) return false;
    if (this.filtroCurso && g.cursoId !== this.filtroCurso) return false;
    if (this.filtroDocente && g.docenteId !== this.filtroDocente) return false;
    return true;
  }

  colorSemestre(grupoId: number): string {
    const g = this.grupos.find(x => x.id === grupoId);
    const sem = this.cursos.find(c => c.id === g?.cursoId)?.semestre;
    return sem ? `hsl(${(sem * 36) % 360} 55% 45%)` : '';
  }
}
