package co.edu.unbosque.proyectonucleo.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.proyectonucleo.dto.CursoDTO;
import co.edu.unbosque.proyectonucleo.entity.Curso;
import co.edu.unbosque.proyectonucleo.exception.CursoEnUsoException;
import co.edu.unbosque.proyectonucleo.exception.CursoNoEncontradoException;
import co.edu.unbosque.proyectonucleo.exception.DatosObligatoriosException;
import co.edu.unbosque.proyectonucleo.exception.IdInvalidoException;
import co.edu.unbosque.proyectonucleo.exception.NombreInvalidoException;
import co.edu.unbosque.proyectonucleo.exception.ReglaNegocioException;
import co.edu.unbosque.proyectonucleo.repository.CursoRepository;
import co.edu.unbosque.proyectonucleo.repository.GrupoRepository;
import co.edu.unbosque.proyectonucleo.repository.HabilitacionRepository;

@Service
@Transactional
public class CursoService {

	@Autowired
	private CursoRepository cursoRepo;

	@Autowired
	private GrupoRepository grupoRepo;

	@Autowired
	private HabilitacionRepository habilitacionRepo;

	@Autowired
	private ValidacionService validacionService;

	@Autowired
	private ModelMapper modelMapper;

	public CursoDTO crear(CursoDTO dto) {
		validar(dto);
		validarFrecuencia(dto.getSesionesPorSemana());
		Curso c = modelMapper.map(dto, Curso.class);
		c.setId(0);
		return modelMapper.map(cursoRepo.save(c), CursoDTO.class);
	}

	@Transactional(readOnly = true)
	public List<CursoDTO> listar() {
		return cursoRepo.findAll().stream().map(c -> modelMapper.map(c, CursoDTO.class)).toList();
	}

	@Transactional(readOnly = true)
	public CursoDTO buscar(Long id) {
		validarId(id);
		return modelMapper.map(cursoRepo.findById(id).orElseThrow(CursoNoEncontradoException::new), CursoDTO.class);
	}

	public CursoDTO actualizar(Long id, CursoDTO dto) {
		validarId(id);
		validar(dto);
		Curso c = cursoRepo.findById(id).orElseThrow(CursoNoEncontradoException::new);
		validarFrecuencia(dto.getSesionesPorSemana());
		c.setNombre(dto.getNombre());
		c.setSesionesPorSemana(dto.getSesionesPorSemana());
		c.setRequiereComputadores(dto.isRequiereComputadores());
		c.setRequiereSillasMoviles(dto.isRequiereSillasMoviles());
		return modelMapper.map(c, CursoDTO.class);
	}

	public int eliminar(Long id) {
		validarId(id);
		if (!cursoRepo.existsById(id)) throw new CursoNoEncontradoException();
		if (grupoRepo.existsByCurso_Id(id) || habilitacionRepo.existsByCurso_Id(id)) throw new CursoEnUsoException();
		cursoRepo.deleteById(id);
		return 0;
	}

	private void validarId(Long id) {
		if (id == null || id <= 0) throw new IdInvalidoException();
	}

	private void validar(CursoDTO dto) {
		if (dto == null) throw new DatosObligatoriosException();
		if (dto.getNombre() == null || dto.getNombre().isBlank() || dto.getNombre().trim().length() > 100) {
			throw new NombreInvalidoException();
		}
	}

	private void validarFrecuencia(int sesiones) {
		ResultadoValidacion r = validacionService.validarFrecuenciaCurso(sesiones);
		if (!r.esValido()) throw new ReglaNegocioException(r.getErrores());
	}
}