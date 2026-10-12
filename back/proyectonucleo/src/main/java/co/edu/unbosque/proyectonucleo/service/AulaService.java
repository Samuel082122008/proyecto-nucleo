package co.edu.unbosque.proyectonucleo.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.proyectonucleo.dto.AulaDTO;
import co.edu.unbosque.proyectonucleo.entity.Aula;
import co.edu.unbosque.proyectonucleo.exception.AulaNoEncontradaException;
import co.edu.unbosque.proyectonucleo.repository.AulaRepository;

@Service
@Transactional
public class AulaService {

	@Autowired
	private AulaRepository aulaRepo;

	@Autowired
	private ModelMapper modelMapper;

	public AulaDTO crear(AulaDTO dto) {
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
		return modelMapper.map(aulaRepo.findById(id).orElseThrow(AulaNoEncontradaException::new), AulaDTO.class);
	}

	public AulaDTO actualizar(Long id, AulaDTO dto) {
		Aula a = aulaRepo.findById(id).orElseThrow(AulaNoEncontradaException::new);
		a.setNombre(dto.getNombre());
		a.setCapacidad(dto.getCapacidad());
		a.setTieneComputadores(dto.isTieneComputadores());
		a.setSillasMoviles(dto.isSillasMoviles());
		return modelMapper.map(a, AulaDTO.class);
	}

	public int eliminar(Long id) {
		if (!aulaRepo.existsById(id)) throw new AulaNoEncontradaException();
		aulaRepo.deleteById(id);
		return 0;
	}
}