package co.edu.unbosque.proyectonucleo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.proyectonucleo.dto.GrupoDTO;
import co.edu.unbosque.proyectonucleo.entity.Curso;
import co.edu.unbosque.proyectonucleo.entity.EstadoGrupo;
import co.edu.unbosque.proyectonucleo.entity.Grupo;
import co.edu.unbosque.proyectonucleo.exception.CursoNoEncontradoException;
import co.edu.unbosque.proyectonucleo.exception.GrupoNoEncontradoException;
import co.edu.unbosque.proyectonucleo.exception.ReglaNegocioException;
import co.edu.unbosque.proyectonucleo.repository.CursoRepository;
import co.edu.unbosque.proyectonucleo.repository.GrupoRepository;

@Service
@Transactional
public class GrupoService {

	@Autowired
	private GrupoRepository grupoRepo;

	@Autowired
	private CursoRepository cursoRepo;

	@Autowired
	private ValidacionService validacionService;

	public GrupoDTO crear(GrupoDTO dto) {
		Curso curso = cursoRepo.findById(dto.getCursoId()).orElseThrow(CursoNoEncontradoException::new);

		ResultadoValidacion r = validacionService.validarCupo(dto.getEstudiantesProyectados());
		if (!r.esValido()) throw new ReglaNegocioException(r.getErrores());

		Grupo g = new Grupo();
		g.setCurso(curso);
		g.setEstudiantesProyectados(dto.getEstudiantesProyectados());
		g.setEstado(EstadoGrupo.ABIERTO);
		return aDTO(grupoRepo.save(g));
	}

	public GrupoDTO cerrar(Long id) {
		Grupo g = grupoRepo.findById(id).orElseThrow(GrupoNoEncontradoException::new);
		g.setEstado(EstadoGrupo.CERRADO);
		return aDTO(g);
	}

	@Transactional(readOnly = true)
	public List<GrupoDTO> listar() {
		return grupoRepo.findAll().stream().map(this::aDTO).toList();
	}

	private GrupoDTO aDTO(Grupo g) {
		GrupoDTO dto = new GrupoDTO();
		dto.setId(g.getId());
		dto.setCursoId(g.getCurso().getId());
		dto.setEstudiantesProyectados(g.getEstudiantesProyectados());
		dto.setEstado(g.getEstado());
		return dto;
	}
}