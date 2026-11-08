package co.edu.unbosque.proyectonucleo.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.proyectonucleo.dto.AulaDTO;
import co.edu.unbosque.proyectonucleo.entity.Aula;
import co.edu.unbosque.proyectonucleo.exception.AulaEnUsoException;
import co.edu.unbosque.proyectonucleo.exception.AulaNoEncontradaException;
import co.edu.unbosque.proyectonucleo.exception.CapacidadInvalidaException;
import co.edu.unbosque.proyectonucleo.exception.DatosObligatoriosException;
import co.edu.unbosque.proyectonucleo.exception.IdInvalidoException;
import co.edu.unbosque.proyectonucleo.exception.NombreInvalidoException;
import co.edu.unbosque.proyectonucleo.repository.AulaRepository;
import co.edu.unbosque.proyectonucleo.repository.SesionRepository;

@Service
@Transactional
public class AulaService {

	@Autowired
	private AulaRepository aulaRepo;

	@Autowired
	private SesionRepository sesionRepo;

	@Autowired
	private ModelMapper modelMapper;

	public AulaDTO crear(AulaDTO dto) {
		validar(dto);
		Aula a = modelMapper.map(dto, Aula.class);
		a.setId(0);
		return modelMapper.map(aulaRepo.save(a), AulaDTO.class);
	}

	@Transactional(readOnly = true)
	public List<AulaDTO> listar() {
		return aulaRepo.findAll().stream().map(a -> modelMapper.map(a, AulaDTO.class)).toList();
	}

	@Transactional(readOnly = true)
	public AulaDTO buscar(Long id) {
		validarId(id);
		return modelMapper.map(aulaRepo.findById(id).orElseThrow(AulaNoEncontradaException::new), AulaDTO.class);
	}

	public AulaDTO actualizar(Long id, AulaDTO dto) {
		validarId(id);
		validar(dto);
		Aula a = aulaRepo.findById(id).orElseThrow(AulaNoEncontradaException::new);
		a.setNombre(dto.getNombre());
		a.setCapacidad(dto.getCapacidad());
		a.setTieneComputadores(dto.isTieneComputadores());
		a.setSillasMoviles(dto.isSillasMoviles());
		return modelMapper.map(a, AulaDTO.class);
	}

	public int eliminar(Long id) {
		validarId(id);
		if (!aulaRepo.existsById(id)) throw new AulaNoEncontradaException();
		if (sesionRepo.existsByAula_Id(id)) throw new AulaEnUsoException();
		aulaRepo.deleteById(id);
		return 0;
	}

	private void validarId(Long id) {
		if (id == null || id <= 0) throw new IdInvalidoException();
	}

	private void validar(AulaDTO dto) {
		if (dto == null) throw new DatosObligatoriosException();
		if (dto.getNombre() == null || dto.getNombre().isBlank() || dto.getNombre().trim().length() > 100) {
			throw new NombreInvalidoException();
		}
		if (dto.getCapacidad() < 1 || dto.getCapacidad() > 1000) throw new CapacidadInvalidaException();
	}
}