package co.edu.unbosque.proyectonucleo.service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.proyectonucleo.entity.Aula;
import co.edu.unbosque.proyectonucleo.entity.Curso;
import co.edu.unbosque.proyectonucleo.entity.DiaSemana;
import co.edu.unbosque.proyectonucleo.entity.EstadoGrupo;
import co.edu.unbosque.proyectonucleo.entity.Grupo;
import co.edu.unbosque.proyectonucleo.entity.ParametroSemestre;
import co.edu.unbosque.proyectonucleo.entity.RestriccionHoraria;
import co.edu.unbosque.proyectonucleo.entity.Sesion;
import co.edu.unbosque.proyectonucleo.exception.ParametroSemestreNoConfiguradoException;
import co.edu.unbosque.proyectonucleo.repository.HabilitacionRepository;
import co.edu.unbosque.proyectonucleo.repository.ParametroSemestreRepository;
import co.edu.unbosque.proyectonucleo.repository.RestriccionHorariaRepository;
import co.edu.unbosque.proyectonucleo.repository.SesionRepository;

@Service
@Transactional
public class ValidacionService {

	@Autowired
	private ParametroSemestreRepository parametroRepo;

	@Autowired
	private SesionRepository sesionRepo;

	@Autowired
	private RestriccionHorariaRepository restriccionRepo;

	@Autowired
	private HabilitacionRepository habilitacionRepo;

	private ParametroSemestre parametros() {
		return parametroRepo.findFirstByOrderByIdAsc()
				.orElseThrow(ParametroSemestreNoConfiguradoException::new);
	}

	public ResultadoValidacion validarSesion(Sesion s) {
		ResultadoValidacion r = new ResultadoValidacion();
		r.unir(validarFranjaHoraria(s));
		r.unir(validarDisponibilidadDocente(s));
		r.unir(validarChoqueDocente(s));
		r.unir(validarChoqueAula(s));
		r.unir(validarAula(s));
		r.unir(validarGrupo(s));
		r.unir(validarDiasConsecutivos(s));
		return r;
	}

	// ESP-05: franja general, sábado y hora de almuerzo
	public ResultadoValidacion validarFranjaHoraria(Sesion s) {
		ResultadoValidacion r = new ResultadoValidacion();
		ParametroSemestre p = parametros();
		LocalTime ini = s.getHoraInicio();
		LocalTime fin = s.getHoraFin();

		if (!ini.isBefore(fin)) {
			r.error("La hora de inicio debe ser anterior a la hora de fin.");
			return r;
		}
		if (Duration.between(ini, fin).toMinutes() != p.getDuracionSesionHoras() * 60L) {
			r.error("Cada sesion debe durar exactamente " + p.getDuracionSesionHoras() + " horas.");
		}
		LocalTime limite = s.getDiaSemana() == DiaSemana.SABADO ? p.getSabadoFin() : p.getFranjaFin();
		if (ini.isBefore(p.getFranjaInicio()) || fin.isAfter(limite)) {
			r.error("La sesion queda fuera de la franja permitida (" + p.getFranjaInicio() + " a " + limite + ").");
		}
		if (ini.isBefore(p.getAlmuerzoFin()) && fin.isAfter(p.getAlmuerzoInicio())) {
			r.error("No se pueden programar clases entre " + p.getAlmuerzoInicio() + " y " + p.getAlmuerzoFin() + ".");
		}
		return r;
	}

	// ESP-02: el docente puede dictar el curso y está disponible en ese bloque
	public ResultadoValidacion validarDisponibilidadDocente(Sesion s) {
		ResultadoValidacion r = new ResultadoValidacion();
		long docenteId = s.getDocente().getId();
		long cursoId = s.getGrupo().getCurso().getId();

		if (!habilitacionRepo.existsByDocente_IdAndCurso_Id(docenteId, cursoId)) {
			r.error("El docente no esta habilitado para dictar este curso.");
		}
		List<RestriccionHoraria> bloques = restriccionRepo.findByDocente_Id(docenteId);
		if (!bloques.isEmpty()) {
			boolean dentro = bloques.stream().anyMatch(b -> b.getDiaSemana() == s.getDiaSemana()
					&& !s.getHoraInicio().isBefore(b.getHoraInicio())
					&& !s.getHoraFin().isAfter(b.getHoraFin()));
			if (!dentro) {
				r.error("El docente no tiene disponibilidad registrada en ese dia y hora.");
			}
		}
		return r;
	}

	// ESP-06
	public ResultadoValidacion validarChoqueDocente(Sesion s) {
		ResultadoValidacion r = new ResultadoValidacion();
		if (sesionRepo.existeChoqueDocente(s.getDocente().getId(), s.getDiaSemana(),
				s.getHoraInicio(), s.getHoraFin(), s.getId())) {
			r.error("El docente ya tiene otra sesion que se cruza con este horario.");
		}
		return r;
	}

	public ResultadoValidacion validarChoqueAula(Sesion s) {
		ResultadoValidacion r = new ResultadoValidacion();
		if (sesionRepo.existeChoqueAula(s.getAula().getId(), s.getDiaSemana(),
				s.getHoraInicio(), s.getHoraFin(), s.getId())) {
			r.error("El aula ya esta ocupada en ese horario.");
		}
		return r;
	}

	// Capacidad y características del aula
	public ResultadoValidacion validarAula(Sesion s) {
		ResultadoValidacion r = new ResultadoValidacion();
		Aula a = s.getAula();
		Grupo g = s.getGrupo();
		Curso c = g.getCurso();
		if (a.getCapacidad() < g.getEstudiantesProyectados()) {
			r.error("El aula tiene capacidad para " + a.getCapacidad() + " y el grupo proyecta "
					+ g.getEstudiantesProyectados() + " estudiantes.");
		}
		if (c.isRequiereComputadores() && !a.isTieneComputadores()) {
			r.error("El curso requiere un aula con computadores.");
		}
		if (c.isRequiereSillasMoviles() && !a.isSillasMoviles()) {
			r.error("El curso requiere un aula con sillas moviles.");
		}
		return r;
	}

	// Grupo abierto, sin cruzarse consigo mismo y sin pasarse de sus sesiones semanales
	public ResultadoValidacion validarGrupo(Sesion s) {
		ResultadoValidacion r = new ResultadoValidacion();
		Grupo g = s.getGrupo();
		if (g.getEstado() == EstadoGrupo.CERRADO) {
			r.error("El grupo esta cerrado y no admite sesiones.");
		}
		if (sesionRepo.existeChoqueGrupo(g.getId(), s.getDiaSemana(), s.getHoraInicio(), s.getHoraFin(), s.getId())) {
			r.error("El grupo ya tiene otra sesion en ese horario.");
		}
		if (sesionRepo.countByGrupo_IdAndIdNot(g.getId(), s.getId()) >= g.getCurso().getSesionesPorSemana()) {
			r.error("El grupo ya tiene todas sus sesiones semanales asignadas (" + g.getCurso().getSesionesPorSemana() + ").");
		}
		return r;
	}

	// ESP-07 (blanda): cursos de 2 o 3 sesiones, mejor en días no consecutivos
	public ResultadoValidacion validarDiasConsecutivos(Sesion s) {
		ResultadoValidacion r = new ResultadoValidacion();
		int porSemana = s.getGrupo().getCurso().getSesionesPorSemana();
		if (porSemana == 2 || porSemana == 3) {
			List<Integer> dias = new ArrayList<>(sesionRepo.findByGrupo_Id(s.getGrupo().getId()).stream()
					.filter(o -> o.getId() != s.getId())
					.map(o -> o.getDiaSemana().ordinal()).toList());
			dias.add(s.getDiaSemana().ordinal());
			if (dias.stream().anyMatch(d -> dias.contains(d + 1))) {
				r.advertencia("Las sesiones de este grupo quedan en dias consecutivos.");
			}
		}
		return r;
	}

	// ESP-03 y ESP-04
	public ResultadoValidacion validarCupo(int estudiantes) {
		ResultadoValidacion r = new ResultadoValidacion();
		ParametroSemestre p = parametros();
		if (estudiantes < p.getCupoMinimo()) {
			r.error("El grupo tiene menos del cupo minimo (" + p.getCupoMinimo() + ").");
		}
		if (estudiantes * 100 > p.getCupoMaximo() * (100 + p.getToleranciaCupo())) {
			r.error("El grupo supera el cupo maximo (" + p.getCupoMaximo() + ") mas la tolerancia del "
					+ p.getToleranciaCupo() + " %.");
		}
		return r;
	}

	// Un curso debe tener entre 1 y sesionesMaxSemana sesiones
	public ResultadoValidacion validarFrecuenciaCurso(int sesiones) {
		ResultadoValidacion r = new ResultadoValidacion();
		int max = parametros().getSesionesMaxSemana();
		if (sesiones < 1 || sesiones > max) {
			r.error("Un curso debe tener entre 1 y " + max + " sesiones por semana.");
		}
		return r;
	}
}