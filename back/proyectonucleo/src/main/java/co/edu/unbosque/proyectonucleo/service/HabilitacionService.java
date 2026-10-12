package co.edu.unbosque.proyectonucleo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.proyectonucleo.dto.HabilitacionDTO;
import co.edu.unbosque.proyectonucleo.entity.Curso;
import co.edu.unbosque.proyectonucleo.entity.Docente;
import co.edu.unbosque.proyectonucleo.entity.Habilitacion;
import co.edu.unbosque.proyectonucleo.exception.CursoNoEncontradoException;
import co.edu.unbosque.proyectonucleo.exception.DocenteNoEncontradoException;
import co.edu.unbosque.proyectonucleo.exception.ReglaNegocioException;
import co.edu.unbosque.proyectonucleo.repository.CursoRepository;
import co.edu.unbosque.proyectonucleo.repository.DocenteRepository;
import co.edu.unbosque.proyectonucleo.repository.HabilitacionRepository;

@Service
@Transactional
public class HabilitacionService {

	@Autowired
	private HabilitacionRepository habilitacionRepo;

	@Autowired
	private DocenteRepository docenteRepo;

	@Autowired
	private CursoRepository cursoRepo;

	public HabilitacionDTO crear(HabilitacionDTO dto) {
		Docente d = docenteRepo.findById(dto.getDocenteId()).orElseThrow(DocenteNoEncontradoException::new);
		Curso c = cursoRepo.findById(dto.getCursoId()).orElseThrow(CursoNoEncontradoException::new);
		if (habilitacionRepo.existsByDocente_IdAndCurso_Id(d.getId(), c.getId())) {
			throw new ReglaNegocioException(List.of("El docente ya esta habilitado para este curso."));
		}
		return aDTO(habilitacionRepo.save(new Habilitacion(d, c)));
	}

	@Transactional(readOnly = true)
	public List<HabilitacionDTO> listar() {
		return habilitacionRepo.findAll().stream().map(this::aDTO).toList();
	}

	public int eliminar(Long id) {
		if (!habilitacionRepo.existsById(id)) throw new IllegalArgumentException("Habilitacion no encontrada");
		habilitacionRepo.deleteById(id);
		return 0;
	}

	private HabilitacionDTO aDTO(Habilitacion h) {
		HabilitacionDTO dto = new HabilitacionDTO();
		dto.setId(h.getId());
		dto.setDocenteId(h.getDocente().getId());
		dto.setCursoId(h.getCurso().getId());
		return dto;
	}
}