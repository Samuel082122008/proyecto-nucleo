package co.edu.unbosque.proyectonucleo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.proyectonucleo.dto.SesionDTO;
import co.edu.unbosque.proyectonucleo.dto.ValidacionDTO;
import co.edu.unbosque.proyectonucleo.entity.Sesion;
import co.edu.unbosque.proyectonucleo.exception.AulaNoEncontradaException;
import co.edu.unbosque.proyectonucleo.exception.DocenteNoEncontradoException;
import co.edu.unbosque.proyectonucleo.exception.GrupoNoEncontradoException;
import co.edu.unbosque.proyectonucleo.exception.SesionNoEncontradaException;
import co.edu.unbosque.proyectonucleo.repository.AulaRepository;
import co.edu.unbosque.proyectonucleo.repository.DocenteRepository;
import co.edu.unbosque.proyectonucleo.repository.GrupoRepository;
import co.edu.unbosque.proyectonucleo.repository.SesionRepository;

@Service
@Transactional
public class HorarioService {

	@Autowired
	private SesionRepository sesionRepo;

	@Autowired
	private GrupoRepository grupoRepo;

	@Autowired
	private DocenteRepository docenteRepo;

	@Autowired
	private AulaRepository aulaRepo;

	@Autowired
	private ValidacionService validacionService;

	public ResultadoSesion asignar(SesionDTO dto) {
		return guardar(new Sesion(), dto);
	}

	public ResultadoSesion mover(Long id, SesionDTO dto) {
		Sesion existente = sesionRepo.findById(id).orElseThrow(SesionNoEncontradaException::new);
		return guardar(existente, dto);
	}

	@Transactional(readOnly = true)
	public ValidacionDTO validar(SesionDTO dto) {
		Sesion candidata = construir(dto);
		candidata.setId(dto.getId() == null ? 0 : dto.getId());
		ResultadoValidacion r = validacionService.validarSesion(candidata);
		return new ValidacionDTO(r.esValido(), r.getErrores(), r.getAdvertencias());
	}

	@Transactional(readOnly = true)
	public List<SesionDTO> listar() {
		return sesionRepo.findAll().stream().map(this::aDTO).toList();
	}

	public int eliminar(Long id) {
		if (!sesionRepo.existsById(id)) throw new SesionNoEncontradaException();
		sesionRepo.deleteById(id);
		return 0;
	}

	private ResultadoSesion guardar(Sesion destino, SesionDTO dto) {
		// Se valida sobre una copia: la sesión ya cargada NO se toca hasta que todo pase
		Sesion candidata = construir(dto);
		candidata.setId(destino.getId());

		ResultadoValidacion r = validacionService.validarSesion(candidata);
		if (!r.esValido()) {
			return ResultadoSesion.conflicto(r.getErrores());
		}

		destino.setGrupo(candidata.getGrupo());
		destino.setDocente(candidata.getDocente());
		destino.setAula(candidata.getAula());
		destino.setDiaSemana(candidata.getDiaSemana());
		destino.setHoraInicio(candidata.getHoraInicio());
		destino.setHoraFin(candidata.getHoraFin());
		sesionRepo.save(destino);

		return ResultadoSesion.exitosa(aDTO(destino), r.getAdvertencias());
	}

	private Sesion construir(SesionDTO dto) {
		Sesion s = new Sesion();
		s.setGrupo(grupoRepo.findById(dto.getGrupoId()).orElseThrow(GrupoNoEncontradoException::new));
		s.setDocente(docenteRepo.findById(dto.getDocenteId()).orElseThrow(DocenteNoEncontradoException::new));
		s.setAula(aulaRepo.findById(dto.getAulaId()).orElseThrow(AulaNoEncontradaException::new));
		s.setDiaSemana(dto.getDiaSemana());
		s.setHoraInicio(dto.getHoraInicio());
		s.setHoraFin(dto.getHoraFin());
		return s;
	}

	private SesionDTO aDTO(Sesion s) {
		SesionDTO dto = new SesionDTO();
		dto.setId(s.getId());
		dto.setGrupoId(s.getGrupo().getId());
		dto.setDocenteId(s.getDocente().getId());
		dto.setAulaId(s.getAula().getId());
		dto.setDiaSemana(s.getDiaSemana());
		dto.setHoraInicio(s.getHoraInicio());
		dto.setHoraFin(s.getHoraFin());
		return dto;
	}

	public static class ResultadoSesion {

		private final int codigo;
		private final SesionDTO sesion;
		private final List<String> errores;
		private final List<String> advertencias;

		private ResultadoSesion(int codigo, SesionDTO sesion, List<String> errores, List<String> advertencias) {
			this.codigo = codigo;
			this.sesion = sesion;
			this.errores = errores;
			this.advertencias = advertencias;
		}

		public static ResultadoSesion exitosa(SesionDTO sesion, List<String> advertencias) {
			return new ResultadoSesion(0, sesion, List.of(), advertencias);
		}

		public static ResultadoSesion conflicto(List<String> errores) {
			return new ResultadoSesion(2, null, errores, List.of());
		}

		public int getCodigo() { return codigo; }
		public SesionDTO getSesion() { return sesion; }
		public List<String> getErrores() { return errores; }
		public List<String> getAdvertencias() { return advertencias; }
	}
}
